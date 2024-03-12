package io.taig.backmail

enum Template:
  case Block(children: List[Template], paragraph: Boolean)
  case Button(children: List[Template], href: Attribute)
  case Headline(children: List[Template])
  case Linebreak
  case Space
  case Text(children: List[Value])
