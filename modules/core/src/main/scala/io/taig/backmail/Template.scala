package io.taig.backmail

enum Template:
  case Alert(children: List[Template], variant: Template.Alert.Variant)
  case Block(children: List[Template], gutter: Template.Block.Gutter)
  case Button(children: List[Template], href: Attribute)
  case Headline(children: List[Template])
  case Link(children: List[Template], href: Attribute)
  case Text(children: List[Value])

object Template:
  object Alert:
    enum Variant:
      case Error
      case Info
      case Warning

  object Block:
    enum Gutter:
      case Large
      case None
      case Small
