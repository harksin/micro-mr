package µ.mr.poc.impl

import µ.mr.poc.api.{KeyValue, µMr}

/** micro MR single thread
  */

trait µMrSingleThread[In, K, V, Out] extends µMr[In, In, K, V, Out] {

  override def µmr(
      in: Src,
      out: Sink,
      num_mappers: Int = 1,
      num_reducers: Int = 1
  ): Unit = {

    val mappedKV = reader(in).flatMap(mapper).toList
    shuffle(mappedKV)
      .map(reducer)
      .foreach(out)

  }
}
