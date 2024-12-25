package io.taig.backmail

abstract class StringPrinter extends Printer:
  override def print(email: Message): String = email.body.map(print).mkString

  final def print(template: Template): String = template match
    case Template.Alert(children, _)      => "***\n" + children.map(print).mkString + "\n***"
    case Template.Block(children, gutter) => children.map(print).mkString + print(gutter)
    case Template.Button(children, href) =>
      val target = href.toList.map(print).mkString
      val label = children.map(print).mkString
      s"$label: $target"
    case Template.Headline(children) => children.map(print).mkString
    case Template.Link(children, href) =>
      val target = href.toList.map(print).mkString
      val label = children.map(print).mkString
      s"$label ($target)"
    case Template.Text(children) => children.map(print).mkString

  final def print(gutter: Template.Block.Gutter): String = gutter match
    case Template.Block.Gutter.Large => "\n\n"
    case Template.Block.Gutter.None  => ""
    case Template.Block.Gutter.Small => "\n"

  def print(value: Value): String
