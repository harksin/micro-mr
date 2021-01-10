package µ.mr.poc.publicapi

import µ.mr.poc.api.SimpleMr
import µ.mr.poc.impl.µMrSingleThread

/** Helper trait
  * combine File source with single thread implementation
  */
trait SimpleMrSingleThread[K, V, Out]
    extends SimpleMr[K, V, Out]
    with µMrSingleThread[String, K, V, Out]
