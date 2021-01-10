package µ.mr.poc.impl

import com.typesafe.scalalogging.LazyLogging
import µ.mr.poc.api.{KeyValue, µMr}

import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.collection.parallel._

/** micro parallel implementation
  * based on par.collection & thread pool
  */

trait µMrParallel[In, K, V, Out]
    extends µMr[In, In, K, V, Out]
    with LazyLogging {

  def availableCpus: Int = Runtime.getRuntime().availableProcessors()

  override def µmr(
      in: Src,
      out: Sink,
      num_mappers: Int = availableCpus,
      num_reducers: Int = math.max(1, availableCpus / 2)
  ): Unit = {

    val pool = new java.util.concurrent.ForkJoinPool(num_mappers)
    val taskSupport = new ForkJoinTaskSupport(pool)

    logger.info(s"reading data")
    val data: List[In] = reader(in).toList
    val groupSize = data.size / math.max(1, num_mappers - 1)
    val parallelRawData = data.grouped(groupSize).zipWithIndex.toList.par

    //set task support
    parallelRawData.tasksupport = taskSupport

    logger.info(s"start mapping & reduce")
    parallelRawData
      .flatMap { mapperData =>
        logger.info(
          s"mapper id : ${mapperData._2} line count : ${mapperData._1.size}"
        )
        mapperData._1.flatMap(mapper)
      }
      // split load according to reducer pool size
      .groupBy(kv => KeyValue.getKeyHash(kv) % num_reducers)
      //shuffle data in each partition and then reduce
      .foreach { reducerData =>
        logger.info(
          s"reducer id : ${reducerData._1} line count : ${reducerData._2.size}  "
        )
        shuffle(reducerData._2.toList)
          .map(reducer)
          .foreach(out) // write only one file
      }

    pool.shutdown()

  }
}
