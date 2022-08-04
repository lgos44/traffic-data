package co.topl.traffic

import cats.effect.{ExitCode, IO, IOApp}
import co.topl.traffic.cli.Arguments
import co.topl.traffic.cli.Parser.argumentParser
import co.topl.traffic.graph.Graph
import co.topl.traffic.graph.Graph.Vertex
import co.topl.traffic.model.{Error, Intersection, Output, TrafficData}
import co.topl.traffic.util.FileUtils
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import scopt.OParser

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    OParser.parse(argumentParser, args, Arguments()) match {
      case Some(arguments) =>
        (for {
          start <- IO.fromEither(Intersection.fromString(arguments.start))
          end <- IO.fromEither(Intersection.fromString(arguments.end))
          file <- FileUtils.readFileToString(arguments.file)
          data <- IO.fromEither(decode[TrafficData](file))
          edges = TrafficProcessor.aggregate(data)
          graph = Graph().addEdges(edges)
          result = graph.shortestPath(Vertex(start), Vertex(end))
          path <- IO.fromOption(result)(Error("No path found for given intersections."))
          _ <- IO.println(Output.fromPath(path).asJson.toString())
        } yield ()).as(ExitCode.Success)
      case _ =>
        IO.raiseError(model.Error("Arguments are invalid.")).as(ExitCode.Error)
    }

}
