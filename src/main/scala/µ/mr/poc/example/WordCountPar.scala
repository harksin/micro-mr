package µ.mr.poc.example

import µ.mr.poc.publicapi.SimpleMrParallel

object WordCountPar extends App with SimpleMrParallel[String, Int, Int] {

  override def mapFunc(in: String) = in
    .replaceAll("""([\p{Punct}])""", "")
    .replaceAll("\\.", "")
    .replaceAll("[0-9]*", "")
    .split(" ")
    .filter(_.size > 3)
    .map(s => (s, 1))

  override def reduceFunc(values: Seq[Int]): Int = values.sum

  logger.info("Starting mr process")
  mr(
    "./sample/*.txt",
    "./output",
    10,
    2
  )
}
