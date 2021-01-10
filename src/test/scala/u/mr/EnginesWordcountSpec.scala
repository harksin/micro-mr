package u.mr

import com.softwaremill.diffx.generic.auto._
import com.softwaremill.diffx.scalatest.DiffMatcher._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import u.mr.helper.{WordCountPar, WordCountSingleThread}

class EnginesWordcountSpec
    extends AnyFlatSpec
    with Matchers
    with GivenWhenThen {

  val engines = List(
    new WordCountPar(),
    new WordCountSingleThread()
  )

  "u-mr engines" should "count word in one line" in {

    Given("a sample of text datas")
    val testData = List(
      "The Actor Model provides a higher level of abstraction level of abstraction"
    )

    val expected: Seq[(String, Int)] = List(
      ("abstraction", 2),
      ("higher", 1),
      ("Model", 1),
      ("of", 2),
      ("level", 2),
      ("a", 1),
      ("provides", 1),
      ("The", 1),
      ("Actor", 1)
    ).sortBy(_._1)

    engines.foreach { engine =>
      Then(
        s"accumulated results from engine [${engine.name}] should match expected values"
      )
      engine.runWithAccumulator(testData).sortBy(_._1) should matchTo(expected)
    }

  }

  "u-mr engines" should "count word in multiple line src" in {
    Given("a sample of text datas")
    val testData = List(
      "The Actor1 Model provides a higher level of abstraction level of abstraction",
      "The Actor2 Model provides a higher level of abstraction level of abstraction",
      "The Actor3 Model provides a higher level of abstraction level of abstraction",
      "The Actor3 Model provides a higher level of abstraction level of abstraction",
      "The Actor3 Model provides a higher level of abstraction level of abstraction",
      "The Actor3 Model provides a higher level of abstraction level of abstraction",
      "The Actor3 Model provides a higher level of abstraction level of abstraction",
      "The Actor3 Model provides a higher level of abstraction level of abstraction"
    )

    val nbOfLine = testData.size

    val expected: Seq[(String, Int)] = List(
      ("abstraction", 2 * nbOfLine),
      ("higher", 1 * nbOfLine),
      ("Model", 1 * nbOfLine),
      ("of", 2 * nbOfLine),
      ("level", 2 * nbOfLine),
      ("a", 1 * nbOfLine),
      ("provides", 1 * nbOfLine),
      ("The", 1 * nbOfLine),
      ("Actor1", 1),
      ("Actor2", 1),
      ("Actor3", 1 * nbOfLine - 2)
    ).sortBy(_._1)

    engines.foreach { engine =>
      Then(
        s"accumulated results from engine [${engine.name}] should match expected values"
      )
      engine.runWithAccumulator(testData).sortBy(_._1) should matchTo(expected)
    }
  }

  "u-mr engines" should "not produce duplicated token" in {

    Given("a sample of text datas")
    val testData = List(
      "The Actor1 Model provides a higher level of abstraction level of abstraction",
      "The Actor2 Model provides a higher level of abstraction level of abstraction",
      "The Actor3 Model provides a higher level of abstraction level of abstraction"
    )

    val expected: Seq[String] = Seq(
      "abstraction",
      "higher",
      "Model",
      "of",
      "level",
      "a",
      "provides",
      "The",
      "Actor1",
      "Actor2",
      "Actor3"
    ).sorted

    engines.foreach { engine =>
      Then(
        s"accumulated results from engine [${engine.name}] should match expected values"
      )
      engine.runWithAccumulator(testData).map(_._1).sorted should matchTo(
        expected
      )
    }

  }

}
