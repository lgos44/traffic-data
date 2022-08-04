package co.topl.traffic

import co.topl.traffic.graph.Graph.{Edge, Vertex}
import co.topl.traffic.model.{Intersection, TrafficData}

object TrafficProcessor {

  trait Aggregation[V, B] {
    def map(in: Edge[V]): B

    def reduce(
      left: B,
      right: B
    ): B

  }

  case class AvgAcc(
    avg: Double,
    count: Int
  )

  def aggregate(
    data: TrafficData
  ): List[Edge[Intersection]] =
    data.trafficMeasurements
      .flatMap(m =>
        m.measurements.map(m =>
          Edge(
            Vertex(Intersection(m.startAvenue, m.startStreet)),
            Vertex(Intersection(m.endAvenue, m.endStreet)),
            m.transitTime
          )
        )
      )
      .groupMapReduce(edge => (edge.from, edge.to))(in => AvgAcc(in.weight, 1)) { case (left, right) =>
        val newAcc = left.count + right.count
        val average = ((left.avg * left.count) + (right.avg * right.count)) / (left.count + right.count)
        AvgAcc(average, newAcc)
      }
      .map { case (k, v) =>
        Edge(k._1, k._2, v.avg)
      }
      .toList

}
