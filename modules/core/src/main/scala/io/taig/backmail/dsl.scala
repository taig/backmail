package io.taig.backmail

object dsl:
  def message(title: String, preheader: Option[String] = None)(message: Template*): Message =
    Message(title, preheader, message.toList)

  def block(paragraph: Boolean)(children: Template*): Template.Block = Template.Block(children.toList, paragraph)
  def block(children: Template*): Template.Block = block(paragraph = true)(children*)

  def button(href: Attribute)(children: Template*): Template.Button = Template.Button(children.toList, href)

  def headline(children: Template*): Template.Headline = Template.Headline(children.toList)

  val linebreak: Template.Linebreak.type = Template.Linebreak

  val space: Template.Space.type = Template.Space

  def text(values: Value*): Template.Text = Template.Text(values.toList)
  def text(value: String): Template.Text = text(Value.Plain(value))

  def plain(value: String): Value.Plain = Value.Plain(value)
  def secret(value: String): Value.Secret = Value.Secret(value)

  def attr(values: Value*): Attribute = Attribute(values.toList)
  def attr(value: String): Attribute = attr(Value.Plain(value))
