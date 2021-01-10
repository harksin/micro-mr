package u.mr.helper

import µ.mr.poc.api.KeyValue
import µ.mr.poc.impl.µMrSingleThread

class WordCountSingleThread
    extends RunnableWithResult
    with µMrSingleThread[String, String, Int, Int] {

  val name = "µMrSingleThread"

  override def parseFunc(rawLine: String): String = identity(rawLine)

  override def mapFunc(in: String): Seq[(String, Int)] =
    in.split(" ").map(s => (s, 1))

  override def reduceFunc(values: Seq[Int]): Int = values.sum

  override def runWithAccumulator(
      datas: List[String]
  ): Seq[(String, Int)] = {

    var results: Seq[(String, Int)] = List.empty

    def accumulator = (kv: KeyValue[String, Int]) =>
      results = results :+ (kv.key, kv.value)

    µmr(() => datas.iterator, accumulator)

    results
  }

}
