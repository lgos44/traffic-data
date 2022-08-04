package co.topl.traffic.model

import co.topl.traffic.graph.Graph

case class Output(
  startIntersection: Intersection,
  endIntersection: Intersection,
  roadSegments: Seq[Intersection],
  totalTransitTime: Double
)

object Output {

  def fromPath(path: Graph.Path[Intersection]): Output = Output(
    startIntersection = path.vertices.head.v,
    endIntersection = path.vertices.last.v,
    roadSegments = path.vertices.drop(1).dropRight(1).map(_.v),
    totalTransitTime = path.distance
  )

}
