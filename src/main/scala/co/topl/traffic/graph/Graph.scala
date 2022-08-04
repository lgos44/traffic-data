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

/** A graph represented by an adjacency list
  * @param adjacency Maps each vertex to its adjacent vertices
  * @tparam V Vertex type parameter
  */
case class Graph[V](
  adjacency: Map[Vertex[V], List[Adjacency[V]]] = Map.empty[Vertex[V], List[Adjacency[V]]]
) {

  /**
    * Adds multiple edges
    * @param edges list of edges
    * @return the updated graph
    */
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

  /**
    * Adds a single edge
    * @param e edge
    * @return the updated graph
    */
  def addEdge(e: Edge[V]): Graph[V] = addEdge(e.from, e.to, e.weight)

  /**
    * Adds a single edge
    * @param from start vertex
    * @param to end vertex
    * @param weight edge weight
    * @return the updated graph
    */
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

  /** Algorithm for finding the shortest path between nodes in a graph
    * @param source Source vertex to calculate the paths from
    * @return Tuple where the first item is a hashmap with distances from source to the vertex at key
    *         while the second item is hashmap containing the previous hops on the shortest path from source to the
    *         vertex at key
    */
  def dijkstra(
    source: Vertex[V]
  ): (Map[Vertex[V], Double], Map[Vertex[V], Vertex[V]]) = {

    /**
      * Recursively applies dijkstra
      * @param unvisited set of unvisited nodes
      * @param dist contains the current distances from the source to other vertices
      * @param prev previous hops on the shortest path from source to the current vertex
      */
    @tailrec
    def dijkstraRec(
      unvisited: Set[Vertex[V]],
      dist: Map[Vertex[V], Double],
      prev: Map[Vertex[V], Vertex[V]]
    ): (Map[Vertex[V], Double], Map[Vertex[V], Vertex[V]]) =
      if (unvisited.isEmpty) (dist, prev)
      else {
        // get unvisited vertex with minimum distance
        val current = unvisited.minBy(dist)
        val newUnvisited = unvisited - current
        val neighbors = adjacency(current)
        val distUpdates = neighbors.collect {
          // If this path is shorter than the current shortest path recorded update dist and prev
          case neighbor if (dist(current) + neighbor.weight) < dist.getOrElse(neighbor.vertex, Double.MaxValue) =>
            neighbor.vertex -> (dist(current) + neighbor.weight)
        }
        val prevUpdates = distUpdates.map(v => v._1 -> current)
        dijkstraRec(newUnvisited, dist ++ distUpdates, prev ++ prevUpdates)
      }

    val distInit = adjacency.view.mapValues(_ => Double.MaxValue).toMap + (source -> 0.0)
    dijkstraRec(adjacency.keys.toSet, distInit, Map.empty)
  }

  /**
    * Builds the shortest path from the source to target vertex
    * @param source start vertex
    * @param target end vertex
    * @return Shortest Path containing the list of hops and total distance to target node
    */
  def shortestPath(
    source: Vertex[V],
    target: Vertex[V]
  ): Option[Path[V]] = {
    val (dist, prev) = dijkstra(source)
    if (prev.contains(target) || source == target)
      Some(Path(path(target)(prev), dist(target)))
    else None
  }

  /**
    * Recursively iterates the prev map to build the path
    * @param to start vertex
    * @param prev previous hops
    * @return list of vertices in path
    */
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
