package co.topl.traffic

import co.topl.traffic.graph.Graph.{Edge, Vertex}
import co.topl.traffic.model.{Intersection, TrafficData}

object TrafficProcessor {

  case class EdgeAcc[A](
    edge: Edge[Intersection],
    acc: A
  )

  def aggregate(data: TrafficData): List[Edge[Intersection]] =
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
      .groupMapReduce(edge => (edge.from, edge.to))(edge => EdgeAcc(edge, 1)) { case (e1, e2) =>
        val newAcc = e1.acc + e2.acc
        val average = ((e1.edge.weight * e1.acc) + (e2.edge.weight * e2.acc)) / (e1.acc + e2.acc)
        EdgeAcc(e1.edge.copy(weight = average), newAcc)
      }
      .map { case (k, v) =>
        Edge(k._1, k._2, v.edge.weight)
      }
      .toList

}
