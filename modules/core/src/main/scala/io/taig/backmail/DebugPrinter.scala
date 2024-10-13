package io.taig.backmail

object DebugPrinter extends Printer:
  override def print(email: Message): String = email.body.map(print).mkString

  def print(template: Template): String = template match
    case Template.Alert(children, _) => "***\n" + children.map(print).mkString + "\n***"
    case Template.Block(children, paragrpah) =>
      children.map(print).mkString + (if paragrpah then "\n\n" else "")
    case Template.Button(children, href) =>
      val target = href.toList.map(print).mkString
      val label = children.map(print).mkString
      s"$label: $target"
    case Template.Headline(children) => s"${children.map(print).mkString}\n\n"
    case Template.Linebreak          => "\n"
    case Template.Link(children, href) =>
      val target = href.toList.map(print).mkString
      val label = children.map(print).mkString
      s"$label: $target"
    case Template.Space          => "\n\n"
    case Template.Text(children) => children.map(print).mkString

  def print(value: Value): String = value match
    case Value.Plain(value)  => value
    case Value.Secret(value) => "*" * value.length
