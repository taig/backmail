package io.taig.backmail

final case class Email(title: String, preheader: Option[String], body: List[Template])
