package µ.mr.poc.config

import com.typesafe.scalalogging.LazyLogging
import scopt.{OParser, OParserBuilder}
import µ.mr.poc.config.Format._

/** Cli to build configuration from parameters
  */
trait Cli extends LazyLogging {

  implicit val FormatReader: scopt.Read[Format.Value] =
    scopt.Read.reads(Format withName _)

  val builder: OParserBuilder[CliConfig] = OParser.builder[CliConfig]
  val parser: OParser[Unit, CliConfig] = {
    import builder._
    OParser.sequence(
      programName("µMr-WordCount"),
      head("µMr", "0.1"),
      opt[String]('s', "source_path")
        .valueName("<./dir/*.txt>")
        .required()
        .action((x, c) => c.copy(sourcePath = x))
        .text(
          """|
             |Defines where data will be read
             |You could either specify a single file <./data/book.txt>
             |or a wild card <./library/*.txt>""".stripMargin
        ),
      opt[String]('o', "output")
        .valueName("<./output>")
        .action((x, c) => c.copy(output = x))
        .text(
          "Allows to select output directory"
        ),
      opt[Format]('f', "format")
        .valueName(s"<${Format.values.mkString(" | ")}>")
        .validate {
          case CSV  => success
          case JSON => failure("json output is not yet implemented")
          case _    => failure("unsupported format")
        }
        .action((x, c) => c.copy(format = x))
        .text("Output format"),
      opt[Int]("mum_mapper")
        .valueName(s"<1->n>")
        .action((x, c) => c.copy(mumMapper = x))
        .text("Number of mappers "),
      opt[Int]("num_reducer")
        .valueName(s"<1->n>")
        .action((x, c) => c.copy(numReducer = x))
        .text("Number of reducers in "),
      opt[Unit]("verbose")
        .action((_, c) => c.copy(verbose = true))
        .text("Verbose mode (not yet implemented)")
    )
  }

  def getConfig(args: Array[String]): Option[CliConfig] =
    OParser.parse(parser, args, CliConfig())

}
