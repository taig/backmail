package io.taig.backmail

object dsl:
  self =>

  def message(title: String, preheader: Option[String] = None)(message: Template*): Message =
    Message(title, preheader, message.toList)

  def block(
      element: Template.Block.Element,
      gutter: Template.Block.Gutter = gutter.none
  )(children: Template*): Template.Block = Template.Block(children = children.toList, gutter, element)

  def inline(element: Template.Inline.Element)(children: Value*): Template.Inline =
    Template.Inline(children = children.toList, element)

  object alert:
    def apply(
        gutter: Template.Block.Gutter = gutter.none,
        variant: Template.Block.Element.Alert.Variant = alert.variant.info
    )(children: Template*): Template.Block =
      block(element = Template.Block.Element.Alert(variant), gutter)(children*)

    def apply(children: Template*): Template.Block = alert()(children*)

    object variant:
      val info: Template.Block.Element.Alert.Variant = Template.Block.Element.Alert.Variant.Info
      val error: Template.Block.Element.Alert.Variant = Template.Block.Element.Alert.Variant.Error
      val warning: Template.Block.Element.Alert.Variant = Template.Block.Element.Alert.Variant.Warning

  def button(href: Attribute, gutter: Template.Block.Gutter = gutter.none)(children: Template*): Template.Block =
    block(element = Template.Block.Element.Button(href), gutter)(children*)

  def headline(gutter: Template.Block.Gutter = gutter.medium, level: Template.Block.Element.Headline.Level = 1)(
      children: Template*
  ): Template.Block = block(element = Template.Block.Element.Headline(level), gutter)(children*)
  def headline(children: Template*): Template.Block = headline()(children*)

  def link(href: Attribute)(children: Value*): Template.Inline =
    `inline`(Template.Inline.Element.Link(href = href))(children*)

  def paragraph(gutter: Template.Block.Gutter = gutter.medium)(children: Template*): Template.Block =
    block(element = Template.Block.Element.Paragraph, gutter)(children*)
  def paragraph(truncated: Boolean)(children: Template*): Template.Block =
    paragraph(gutter = gutter.medium(enabled = !truncated))(children*)
  def paragraph(children: Template*): Template.Block = paragraph()(children*)

  object section:
    def apply(gutter: Template.Block.Gutter = gutter.large)(children: Template*): Template.Block =
      block(element = Template.Block.Element.Section, gutter)(children*)
    def apply(truncated: Boolean)(children: Template*): Template.Block =
      section(gutter = gutter.large(enabled = !truncated))(children*)
    def apply(children: Template*): Template.Block = section(truncated = false)(children*)

  def text(values: Value*): Template.Inline = `inline`(element = Template.Inline.Element.Text)(values*)
  def text(value: String): Template.Inline = text(Value.Plain(value))

  object gutter:
    val large: Template.Block.Gutter = Template.Block.Gutter.Large
    def large(enabled: Boolean): Template.Block.Gutter = if enabled then large else none

    val medium: Template.Block.Gutter = Template.Block.Gutter.Medium
    def medium(enabled: Boolean): Template.Block.Gutter = if enabled then medium else none

    val none: Template.Block.Gutter = Template.Block.Gutter.None

    val small: Template.Block.Gutter = Template.Block.Gutter.Small
    def small(enabled: Boolean): Template.Block.Gutter = if enabled then small else none

  def plain(value: String): Value = Value.Plain(value)
  def secret(value: String): Value = Value.Secret(value)

  def attr(values: Value*): Attribute = Attribute(values.toList)
  def attr(value: String): Attribute = attr(Value.Plain(value))

  implicit def stringToValue(value: String): Value = plain(value)
  implicit def valueToTemplate(value: Value): Template.Inline = text(value)
  implicit def stringToTemplate(value: String): Template.Inline = stringToValue.andThen(valueToTemplate).apply(value)
