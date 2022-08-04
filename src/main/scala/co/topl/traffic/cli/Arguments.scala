package co.topl.traffic.cli

import java.io.File

case class Arguments(
  start: String = "A1",
  end: String = "B2",
  file: File = new File("./sample-data.json")
)
