package co.topl.traffic.model

case class Error(msg: String) extends Exception(msg)
