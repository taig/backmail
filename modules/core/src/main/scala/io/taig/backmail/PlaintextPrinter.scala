package io.taig.backmail

object PlaintextPrinter extends Printer:
  override def print(email: Message): String = email.body.map(print).mkString

  def print(template: Template): String = template match
    case Template.Block(children, paragraph) =>
      children.map(print).mkString + (if paragraph then "\n\n" else "")
    case Template.Button(children, href) =>
      val target = href.toList.map(apply).mkString
      val label = children.map(print).mkString
      s"$label: $target"
    case Template.Headline(children) => s"${children.map(print).mkString}\n\n"
    case Template.Linebreak          => "\n"
    case Template.Space              => "\n\n"
    case Template.Text(children)     => children.map(apply).mkString

  def apply(value: Value): String = value match
    case Value.Plain(value)  => value
    case Value.Secret(value) => value
