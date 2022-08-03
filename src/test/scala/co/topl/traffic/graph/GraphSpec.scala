package co.topl.traffic.graph

import co.topl.traffic.BaseSpec
import co.topl.traffic.graph.Graph.Vertex

class GraphSpec extends BaseSpec {
  val tolerance: Double = 0.001d

  "Dijkstra algorithm" should {
    "find the shortest path" in {
      val v = (0 to 7).map(n => Vertex(n.toString))
      val g = Graph()
        .addEdge(v(4), v(5), 0.35)
        .addEdge(v(5), v(4), 0.35)
        .addEdge(v(4), v(7), 0.37)
        .addEdge(v(5), v(7), 0.28)
        .addEdge(v(7), v(5), 0.28)
        .addEdge(v(5), v(1), 0.32)
        .addEdge(v(0), v(4), 0.38)
        .addEdge(v(0), v(2), 0.26)
        .addEdge(v(7), v(3), 0.39)
        .addEdge(v(1), v(3), 0.29)
        .addEdge(v(2), v(7), 0.34)
        .addEdge(v(6), v(2), 0.40)
        .addEdge(v(3), v(6), 0.52)
        .addEdge(v(6), v(0), 0.58)
        .addEdge(v(6), v(4), 0.93)

      val path = g.shortestPath(v(0), v(6))
      path shouldBe defined
      path.get.vertices should be(
        List(Vertex("0"), Vertex("2"), Vertex("7"), Vertex("3"), Vertex("6"))
      )
      path.get.distance should be(1.51d +- tolerance)
    }
  }

}
