package io.taig.backmail

final case class Message(title: String, summary: Option[String], body: List[Template])
