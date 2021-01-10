package u.mr.helper

trait RunnableWithResult {
  def runWithAccumulator(datas: List[String]): Seq[(String, Int)]
  val name: String
}
