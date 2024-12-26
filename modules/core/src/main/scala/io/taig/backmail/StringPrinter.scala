package io.taig.backmail

import io.taig.backmail.Template.Block
import io.taig.backmail.Template.Inline
import io.taig.backmail.Template.Inline.Element

import scala.annotation.targetName

abstract class StringPrinter extends Printer:
  override def print(email: Message): String = email.body.map(print).mkString

  final def print(template: Template): String = template match
    case Block(children, gutter, element) => print(element, gutter, children)
    case Inline(children, element)        => print(element, children)

  final def print(element: Template.Block.Element, gutter: Template.Block.Gutter, children: List[Template]): String =
    val tag = element match
      case Template.Block.Element.Alert(_) =>
        s"""***
           |${print(children)}
           |***""".stripMargin
      case Template.Block.Element.Button(href) => s"${print(children)}: ${print(href)}"
      case Template.Block.Element.Headline(_)  => print(children)
      case Template.Block.Element.Paragraph    => print(children)
      case Template.Block.Element.Section      => print(children)

    tag + print(gutter)

  final def print(element: Template.Inline.Element, children: List[Value]): String = element match
    case Element.Link(href) => s"${print(children)} (${print(href)})"
    case Element.Text       => print(children)

  final def print(gutter: Template.Block.Gutter): String = gutter match
    case Template.Block.Gutter.None   => ""
    case Template.Block.Gutter.Small  => "\n"
    case Template.Block.Gutter.Medium => "\n\n"
    case Template.Block.Gutter.Large  => "\n\n\n"

  @targetName("printTemplates")
  final def print(children: List[Template]): String = children.map(print).mkString

  @targetName("printValues")
  final def print(children: List[Value]): String = children.map(print).mkString

  @targetName("printAttribute")
  final def print(attribute: Attribute): String = attribute.toList.map(print).mkString

  def print(value: Value): String
