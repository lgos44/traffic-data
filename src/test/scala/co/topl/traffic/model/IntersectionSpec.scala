package co.topl.traffic.model

import co.topl.traffic.BaseSpec

class IntersectionSpec extends BaseSpec {

  "Intersection" should {
    "parse from string" in {
      Intersection.fromString("F24") should be(Right(Intersection("F", "24")))
      Intersection.fromString("A1") should be(Right(Intersection("A", "1")))
    }
    "be invalid" in {
      Intersection.fromString("aF24") should be(Left(Error("Input is not a valid intersection")))
      Intersection.fromString("F24a") should be(Left(Error("Input is not a valid intersection")))
    }
  }

}
