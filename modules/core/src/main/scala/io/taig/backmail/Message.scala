package io.taig.backmail

final case class Message(title: String, preheader: Option[String], body: List[Template])
