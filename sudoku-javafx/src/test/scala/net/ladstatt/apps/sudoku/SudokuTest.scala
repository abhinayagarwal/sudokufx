package net.ladstatt.apps.sudoku

import java.io.File

import net.ladstatt.core.Utils
import net.ladstatt.opencv.OpenCV
import org.junit.Assert._
import org.junit.Test

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.util.{Success, Try}


/**
 * Created by lad on 05.05.14.
 */
class SudokuTest  {

  OpenCV.loadNativeLib("../lib/libopencv_java310.so")

  val refCellNumbers: Seq[(Int, Double)] = {
    val lines: Iterator[String] = Source.fromFile(new File("src/test/resources/cellNumbers69.csv")).getLines
    (for (l <- lines) yield {
      val a = l.split(',')
      (a(0).toInt, a(1).toDouble)
    }).toSeq
  }


  // compares individual detection results with a reference file
  @Test def testDetect(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    assertEquals(81.toLong, refCellNumbers.size.toLong)
    val cells = Await.result(Future.sequence(OpenCVTestContext.sudoku69.cellDetector.futureSCells), Duration.Inf)
    var i = 0
    for (c <- cells) {
      assertEquals(refCellNumbers(i)._1.toLong, c.value.toLong)
      assertEquals(refCellNumbers(i)._2, c.quality, 0.000001D)
      i = i + 1
    }
  }



}
