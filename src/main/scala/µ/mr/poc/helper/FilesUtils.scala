package µ.mr.poc.helper

import better.files._
import com.typesafe.scalalogging.LazyLogging

/** Helper function to read files with wildcard and create output file
  */

object FilesUtils extends LazyLogging {
  private val globReg = "^.*\\/(\\*\\.[a-z]*)$".r

  def readFromPath(path: String): Option[() => Iterator[String]] = path match {
    case globReg(pattern) =>
      val truncatedPath = path.take(1) match {
        case "/" => "/" + path.split("/").dropRight(1).mkString("/")
        case _   => path.split("/").dropRight(1).mkString("/")
      }
      Some(readFolder(truncatedPath, pattern))
    case _ =>
      val file = File(path)
      if (file.isDirectory) {
        logger.error(
          "read directory without define a glob patten (*.txt, *.json ...) is not allowed"
        )
        None
      } else {
        Some(() => file.lineIterator)
      }
  }

  def readFolder(path: String, pattern: String): () => Iterator[String] = () =>
    File(path)
      .glob(pattern)
      .flatMap(_.lineIterator)

  def writer(path: String): Int => File = (reducerId: Int) =>
    File(s"path/output-$reducerId.txt")

  def outputFile(path: String): File =
    File(s"$path/output.txt")
      .createFileIfNotExists(createParents = true)
      .clear()

}
