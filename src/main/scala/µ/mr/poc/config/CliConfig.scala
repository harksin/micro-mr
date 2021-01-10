package µ.mr.poc.config

import µ.mr.poc.config.Format.Format

case class CliConfig(
    sourcePath: String = "./data/*.txt",
    output: String = "./output",
    format: Format = Format.CSV,
    mumMapper: Int = 10,
    numReducer: Int = 2,
    verbose: Boolean = false
)

object Format extends Enumeration {
  type Format = Value
  val CSV, JSON = Value
}
