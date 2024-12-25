package io.taig.backmail

object dsl:
  self =>

  def message(title: String, preheader: Option[String] = None)(message: Template*): Message =
    Message(title, preheader, message.toList)

  object alert:
    def apply(variant: Template.Alert.Variant)(children: Template*): Template.Alert =
      Template.Alert(children = children.toList, variant)

    def apply(children: Template*): Template.Alert = alert(variant = variant.info)(children*)

    object variant:
      val info: Template.Alert.Variant = Template.Alert.Variant.Info
      val error: Template.Alert.Variant = Template.Alert.Variant.Error
      val warning: Template.Alert.Variant = Template.Alert.Variant.Warning

  object block:
    def apply(gutter: Template.Block.Gutter)(children: Template*): Template.Block =
      Template.Block(children.toList, gutter)
    def apply(truncated: Boolean)(children: Template*): Template.Block =
      block(gutter = gutter.large(enabled = !truncated))(children*)
    def apply(children: Template*): Template.Block = block(truncated = false)(children*)

    object gutter:
      val large: Template.Block.Gutter = Template.Block.Gutter.Large
      def large(enabled: Boolean): Template.Block.Gutter = if enabled then large else none

      val none: Template.Block.Gutter = Template.Block.Gutter.None

      val small: Template.Block.Gutter = Template.Block.Gutter.Small
      def small(enabled: Boolean): Template.Block.Gutter = if enabled then small else none

  def button(href: Attribute)(children: Template*): Template.Button = Template.Button(children.toList, href)

  def headline(children: Template*): Template.Block =
    block(gutter = block.gutter.large)(Template.Headline(children.toList))
  def headline(value: String): Template.Block = headline(text(value))

  def link(href: Attribute)(children: Template*): Template.Link = Template.Link(children.toList, href)

  def paragraph(truncated: Boolean)(values: Value*): Template.Block =
    block(gutter = block.gutter.large(enabled = !truncated))(text(values*))
  def paragraph(values: Value*): Template.Block = paragraph(truncated = false)(values*)
  def paragraph(truncated: Boolean)(value: String): Template.Block = paragraph(truncated)(Value.Plain(value))
  def paragraph(value: String): Template.Block = paragraph(truncated = false)(value)

  def text(values: Value*): Template.Text = Template.Text(values.toList)
  def text(value: String): Template.Text = text(Value.Plain(value))

  def plain(value: String): Value.Plain = Value.Plain(value)
  def secret(value: String): Value.Secret = Value.Secret(value)

  def attr(values: Value*): Attribute = Attribute(values.toList)
  def attr(value: String): Attribute = attr(Value.Plain(value))
