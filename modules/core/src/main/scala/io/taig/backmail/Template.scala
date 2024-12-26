package io.taig.backmail

sealed abstract class Template extends Product with Serializable

object Template:
  final case class Block(
      children: List[Template],
      gutter: Template.Block.Gutter,
      element: Template.Block.Element
  ) extends Template

  object Block:
    enum Element:
      case Alert(variant: Template.Block.Element.Alert.Variant)
      case Button(href: Attribute)
      case Headline(level: Template.Block.Element.Headline.Level)
      case Paragraph
      case Section

    object Element:
      object Alert:
        enum Variant:
          case Error
          case Info
          case Warning

      object Headline:
        type Level = 1 | 2 | 3 | 4 | 5 | 6

    enum Gutter:
      case Large
      case Medium
      case None
      case Small

  final case class Inline(children: List[Value], element: Template.Inline.Element) extends Template

  object Inline:
    enum Element:
      case Link(href: Attribute)
      case Text
