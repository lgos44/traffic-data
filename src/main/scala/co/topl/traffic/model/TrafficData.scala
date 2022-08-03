package co.topl.traffic.model

case class Measurement(
  startAvenue: String,
  startStreet: String,
  endAvenue: String,
  endStreet: String,
  transitTime: Double
)

case class TrafficMeasurement(
  measurementTime: Long,
  measurements: List[Measurement]
)

case class TrafficData(trafficMeasurements: List[TrafficMeasurement])
