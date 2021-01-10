package µ.mr.poc.publicapi

import µ.mr.poc.api.SimpleMr
import µ.mr.poc.impl.µMrParallel

/** Helper trait
  * combine File source with parallel implementation
  */
trait SimpleMrParallel[K, V, Out]
    extends SimpleMr[K, V, Out]
    with µMrParallel[String, K, V, Out]
