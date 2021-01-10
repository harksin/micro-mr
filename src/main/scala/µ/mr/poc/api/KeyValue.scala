package µ.mr.poc.api

import scala.util.hashing.MurmurHash3

/** a tuple2  typed for readability
  */
case class KeyValue[K, V](key: K, value: V)
case class Key[K](key: K)

object KeyValue {
  def fromTuple[K, V](kv: (K, V)): KeyValue[K, V] = KeyValue(kv._1, kv._2)
  def getKeyHash[K, V](keyValue: KeyValue[K, V]): Int =
    MurmurHash3.productHash(Key(keyValue.key)).abs
}
