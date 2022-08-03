package co.topl.traffic.model

case class Segment(
  from: Intersection,
  to: Intersection,
  duration: Double
)

case class Output(
  start: Intersection,
  end: Intersection,
  roadSegments: Seq[Segment],
  totalTransitTime: Double
)
