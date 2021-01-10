package µ.mr.poc.api

import com.typesafe.scalalogging.LazyLogging

/** micro MR api
  *
  * micro mr implementation :
  * 1/ define 3 function parseFunc, mapFunc, reduceFunc
  * 2/ provide an engine which compose the mr pipeline  reader->mapper->shuffle->reducer->out
  *
  * @tparam Raw raw data type read from file, String, Array[Byte]
  * @tparam In  type consumed by mapper and outputted by parser
  * @tparam K   Key generated by mapper
  * @tparam V   Value generated by mapper
  * @tparam Out Values produced by reducer
  */

trait µMr[Raw, In, K, V, Out] extends LazyLogging {

  type Src = () => Iterator[Raw]
  type Sink = KeyValue[K, Out] => Unit

  /** implement this function to deserialize source data
    * @param rawLine a line read from a file or a chunk of data
    * @return
    */
  def parseFunc(rawLine: Raw): In

  /** engine default reader
    * @param src iterator of raw line
    * @return
    */
  protected def reader(src: Src): Iterator[In] = src().map(parseFunc)

  /** mapFunc is the second step of a map reduce pipeline.
    * implement this function to transform a line into a sequence of (key, value)
    *
    * @param in parsed data
    * @return
    */
  def mapFunc(in: In): Iterable[(K, V)]

  /** engine default mapper
    * @param in parsed data
    * @return
    */
  protected def mapper(in: In): Iterable[KeyValue[K, V]] =
    mapFunc(in).map(KeyValue.fromTuple)

  /** engine default group by key all values (shuffle)
    * @param keyValues list of tuple (key,value)
    * @return
    */
  def shuffle(
      keyValues: List[KeyValue[K, V]]
  ): Iterable[KeyValue[K, Iterable[V]]] = keyValues
    .groupMap(_.key)(kv => kv.value)
    .map { g => KeyValue(g._1, g._2) }

  /** reduceFunc is the third step of a map reduce pipeline.
    * implement this function to transform (reduce) sequence of Value grouped by key
    */
  def reduceFunc(values: Seq[V]): Out

  /** engine default reducer
    */
  protected def reducer(keyValue: KeyValue[K, Iterable[V]]): KeyValue[K, Out] =
    keyValue.copy(value = reduceFunc(keyValue.value.toSeq))

  /** engine have to implement there logic here
    * @param in source iterator
    * @param out result type
    * @param num_mappers mapper parallelism
    * @param num_reducers reducer parallelism
    */
  def µmr(
      in: Src,
      out: Sink,
      num_mappers: Int, //default cpi -1 dams la conf
      num_reducers: Int
  ): Unit

}
