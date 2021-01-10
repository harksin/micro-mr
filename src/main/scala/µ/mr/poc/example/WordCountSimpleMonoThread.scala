package µ.mr.poc.example

import better.files._
import µ.mr.poc.api.KeyValue
import µ.mr.poc.helper.FilesUtils
import µ.mr.poc.publicapi.SimpleMrSingleThread

object WordCountSimpleMonoThread
    extends App
    with SimpleMrSingleThread[String, Int, Int] {

  override def mapFunc(in: String) = in.split(" ").map(s => (s, 1))

  override def reduceFunc(values: Seq[Int]): Int = values.sum

  val outputFile: File = FilesUtils.outputFile("./output")

  µmr(
    FilesUtils.readFolder("./sample/", "*.txt"),
    (kv: KeyValue[String, Int]) => {
      outputFile.appendLine(s"${kv.key}, ${kv.value}")
    }
  )

}
