package co.topl.traffic.graph

import co.topl.traffic.graph.Graph._

import scala.annotation.tailrec

object Graph {
  case class Vertex[V](v: V) extends AnyVal

  case class Adjacency[V](
    vertex: Vertex[V],
    weight: Double
  )

  case class Edge[V](
    from: Vertex[V],
    to: Vertex[V],
    weight: Double
  )

  case class Path[V](
    vertices: List[Vertex[V]],
    distance: Double
  )

}

case class Graph[V](
  adjacency: Map[Vertex[V], List[Adjacency[V]]] = Map.empty[Vertex[V], List[Adjacency[V]]]
) {

  def addEdges(edges: List[Edge[V]]): Graph[V] = {
    @tailrec
    def addEdgesRec(
      edges: List[Edge[V]],
      acc: Graph[V]
    ): Graph[V] = edges match {
      case Nil => acc
      case h :: t => addEdgesRec(t, acc.addEdge(h))
    }
    addEdgesRec(edges, this)
  }

  def addEdge(e: Edge[V]): Graph[V] = addEdge(e.from, e.to, e.weight)

  def addEdge(
    from: Vertex[V],
    to: Vertex[V],
    weight: Double
  ): Graph[V] = {
    val adjs = adjacency.getOrElse(from, List.empty)
    Graph(
      adjacency.updated(from, adjs :+ Adjacency(to, weight))
    )
  }

  def dijkstra(
    source: Vertex[V]
  ): (Map[Vertex[V], Double], Map[Vertex[V], Vertex[V]]) = {

    @tailrec
    def dijkstraRec(
      unvisited: Set[Vertex[V]],
      dist: Map[Vertex[V], Double],
      prev: Map[Vertex[V], Vertex[V]]
    ): (Map[Vertex[V], Double], Map[Vertex[V], Vertex[V]]) =
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
    source: Vertex[V],
    target: Vertex[V]
  ): Option[Path[V]] = {
    val (dist, prev) = dijkstra(source)
    if (prev.contains(target) || source == target)
      Some(Path(path(target)(prev), dist(target)))
    else None
  }

  def path(to: Vertex[V])(prev: Map[Vertex[V], Vertex[V]]): List[Vertex[V]] = {
    @tailrec
    def pathRec(
      x: Vertex[V],
      acc: List[Vertex[V]]
    ): List[Vertex[V]] = prev.get(x) match {
      case None => x :: acc
      case Some(y) => pathRec(y, x :: acc)
    }
    pathRec(to, List.empty)
  }

}
