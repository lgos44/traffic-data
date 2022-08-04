package co.topl.traffic.cli

import scopt.{OParser, OParserBuilder}

import java.io.File

object Parser {
  val builder: OParserBuilder[Arguments] = OParser.builder[Arguments]

  val argumentParser: OParser[Unit, Arguments] = {
    import builder._
    OParser.sequence(
      programName("traffic-path"),
      opt[File]('f', "file")
        .required()
        .valueName("<file>")
        .action((x, c) => c.copy(file = x))
        .text("Input file containing traffic data"),
      opt[String]('s', "start")
        .required()
        .valueName("<start>")
        .action((x, c) => c.copy(start = x))
        .text("Start intersection, identified by the combination of the avenue and street names, e.g. F24"),
      opt[String]('e', "end")
        .required()
        .valueName("<end>")
        .action((x, c) => c.copy(end = x))
        .text("End intersection, identified by the combination of the avenue and street names, e.g. F24")
    )
  }

}
