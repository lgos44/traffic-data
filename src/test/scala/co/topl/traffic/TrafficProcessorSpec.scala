package co.topl.traffic

import co.topl.traffic.graph.Graph.{Edge, Vertex}
import co.topl.traffic.model.{Intersection, Measurement, TrafficData, TrafficMeasurement}

class TrafficProcessorSpec extends BaseSpec {

  "TrafficProcessor" should {
    "aggregate measurements into graph edges" in {
      val trafficData = TrafficData(
        trafficMeasurements = List(
          TrafficMeasurement(
            measurementTime = 1,
            measurements = List(
              Measurement(
                startAvenue = "A",
                startStreet = "1",
                endAvenue = "A",
                endStreet = "2",
                transitTime = 1d
              ),
              Measurement(
                startAvenue = "B",
                startStreet = "1",
                endAvenue = "B",
                endStreet = "2",
                transitTime = 2d
              ),
              Measurement(
                startAvenue = "C",
                startStreet = "3",
                endAvenue = "C",
                endStreet = "4",
                transitTime = 12d
              )
            )
          ),
          TrafficMeasurement(
            measurementTime = 1,
            measurements = List(
              Measurement(
                startAvenue = "A",
                startStreet = "1",
                endAvenue = "A",
                endStreet = "2",
                transitTime = 3d
              ),
              Measurement(
                startAvenue = "B",
                startStreet = "1",
                endAvenue = "B",
                endStreet = "2",
                transitTime = 4d
              )
            )
          ),
          TrafficMeasurement(
            measurementTime = 1,
            measurements = List(
              Measurement(
                startAvenue = "B",
                startStreet = "1",
                endAvenue = "B",
                endStreet = "2",
                transitTime = 15d
              ),
              Measurement(
                startAvenue = "C",
                startStreet = "3",
                endAvenue = "C",
                endStreet = "4",
                transitTime = 2d
              )
            )
          )
        )
      )
      TrafficProcessor.aggregate(trafficData) shouldBe (
        List(
          Edge(Vertex(Intersection("C", "3")), Vertex(Intersection("C", "4")), 7.0),
          Edge(Vertex(Intersection("A", "1")), Vertex(Intersection("A", "2")), 2.0),
          Edge(Vertex(Intersection("B", "1")), Vertex(Intersection("B", "2")), 7.0)
        )
      )

    }
  }

}
