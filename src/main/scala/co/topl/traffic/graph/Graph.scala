package co.topl.traffic.graph

import co.topl.traffic.graph.Graph._

import scala.annotation.tailrec

object Graph {
  case class Vertex(id: String) extends AnyVal

  case class Adjacency(
    vertex: Vertex,
    weight: Double
  )

  case class Edge(
    from: Vertex,
    to: Vertex,
    weight: Double
  )

  case class Path(
    vertices: List[Vertex],
    distance: Double
  )

}

case class Graph(adjacency: Map[Vertex, List[Adjacency]] = Map.empty) {

  type Result = Either[String, Path]

  def addEdge(e: Edge): Graph = addEdge(e.from, e.to, e.weight)

  def addEdge(
    from: Vertex,
    to: Vertex,
    weight: Double
  ): Graph = {
    val adjs = adjacency.getOrElse(from, List.empty)
    Graph(
      adjacency.updated(from, adjs :+ Adjacency(to, weight))
    )
  }

  def dijkstra(
    source: Vertex
  ): (Map[Vertex, Double], Map[Vertex, Vertex]) = {

    @tailrec
    def dijkstraRec(
      unvisited: Set[Vertex],
      dist: Map[Vertex, Double],
      prev: Map[Vertex, Vertex]
    ): (Map[Vertex, Double], Map[Vertex, Vertex]) =
      if (unvisited.isEmpty) (dist, prev)
      else {
        val current = unvisited.minBy(dist)
        val newUnvisited = unvisited - current
        val neighbors = adjacency(current)
        val distUpdates = neighbors.collect {
          case neighbor if (dist(current) + neighbor.weight) < dist.getOrElse(neighbor.vertex, Double.MaxValue) =>
            neighbor.vertex -> (dist(current) + neighbor.weight)
        }
        val prevUpdates = distUpdates.map(v => v._1 -> current)
        dijkstraRec(newUnvisited, dist ++ distUpdates, prev ++ prevUpdates)
      }

    val distInit = adjacency.view.mapValues(_ => Double.MaxValue).toMap + (source -> 0.0)
    dijkstraRec(adjacency.keys.toSet, distInit, Map.empty)
  }

  def shortestPath(
    source: Vertex,
    target: Vertex
  ): Option[Path] = {
    val (dist, prev) = dijkstra(source)
    if (prev.contains(target) || source == target)
      Some(Path(path(target)(prev), dist(target)))
    else None
  }

  def path(to: Vertex)(prev: Map[Vertex, Vertex]): List[Vertex] = {
    @tailrec
    def pathRec(
      x: Vertex,
      acc: List[Vertex]
    ): List[Vertex] = prev.get(x) match {
      case None => x :: acc
      case Some(y) => pathRec(y, x :: acc)
    }
    pathRec(to, List.empty)
  }

}
