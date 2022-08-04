package co.topl.traffic.util

import cats.effect.{IO, Resource}

import java.io.{BufferedReader, File, FileReader}
import scala.jdk.CollectionConverters._

object FileUtils {

  def bufferedReader(f: File): Resource[IO, BufferedReader] =
    Resource.make {
      IO(new BufferedReader(new FileReader(f)))
    } { fileReader =>
      IO(fileReader.close()).handleErrorWith(_ => IO.unit)
    }

  def readFileToString(file: File): IO[String] = bufferedReader(file)
    .use(reader => IO(reader.lines().iterator().asScala.mkString))

}
