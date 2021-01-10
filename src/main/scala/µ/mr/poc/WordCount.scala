package µ.mr.poc

import µ.mr.poc.config.Cli
import µ.mr.poc.publicapi.SimpleMrParallel

object WordCount extends App with SimpleMrParallel[String, Int, Int] with Cli {

  def tokenize(line: String): List[String] = line
    .replaceAll("""([\p{Punct}])""", " ")
    .split(" ")
    .toList

  def filterToken(token: String): Boolean = token.size > 3

  override def mapFunc(in: String) = tokenize(in)
    .filter(filterToken)
    .map(s => (s, 1))

  override def reduceFunc(values: Seq[Int]): Int = values.sum

  getConfig(args) match {
    case Some(config) =>
      logger.info(s"Starting µMr process with \n $config")
      mr(
        config.sourcePath,
        config.output,
        config.mumMapper,
        config.numReducer
      )
    case _ =>
      logger.error("invalid configuration")
      System.exit(1)
  }

}
