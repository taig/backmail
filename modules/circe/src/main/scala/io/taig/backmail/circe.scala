package io.taig.backmail

import io.circe.Encoder
import io.circe.JsonObject
import io.circe.syntax.*
import io.circe.Decoder
import io.circe.DecodingFailure
import io.circe.Json

object circe:
  private object Key:
    val Block: String = "block"
    val Body: String = "body"
    val Button: String = "button"
    val Children: String = "children"
    val Headline: String = "headline"
    val Href: String = "href"
    val Linebreak: String = "linebreak"
    val Link: String = "link"
    val Paragraph: String = "paragraph"
    val Plain: String = "plain"
    val Preheader: String = "preheader"
    val Secret: String = "secret"
    val Space: String = "space"
    val Text: String = "text"
    val Title: String = "title"
    val Type: String = "type"
    val Value: String = "value"

  given Decoder[Value] = cursor =>
    cursor
      .get[String](Key.Type)
      .flatMap:
        case Key.Plain  => cursor.get[String](Key.Value).map(Value.Plain.apply)
        case Key.Secret => cursor.get[String](Key.Value).map(Value.Secret.apply)
        case tpe        => Left(DecodingFailure(s"Unknown: '$tpe'", cursor.downField(Key.Type).history))

  given Decoder[Attribute] = Decoder[List[Value]].map(Attribute.apply)

  given Encoder[Attribute] = _.toList.asJson

  given Encoder.AsObject[Value] =
    case Value.Plain(value)  => JsonObject(Key.Type := Key.Plain, Key.Value := value)
    case Value.Secret(value) => JsonObject(Key.Type := Key.Secret, Key.Value := value)

  given Decoder[Template] = cursor =>
    cursor
      .get[String](Key.Type)
      .flatMap:
        case Key.Block =>
          for
            value <- cursor.get[Json](Key.Value)
            children <- value.hcursor.get[List[Template]](Key.Children)
            paragraph <- value.hcursor.get[Boolean](Key.Paragraph)
          yield Template.Block(children, paragraph)
        case Key.Button =>
          for
            value <- cursor.get[Json](Key.Value)
            children <- value.hcursor.get[List[Template]](Key.Children)
            href <- value.hcursor.get[Attribute](Key.Href)
          yield Template.Button(children, href)
        case Key.Headline =>
          for
            value <- cursor.get[Json](Key.Value)
            children <- value.hcursor.get[List[Template]](Key.Children)
          yield Template.Headline(children)
        case Key.Linebreak => Right(Template.Linebreak)
        case Key.Link =>
          for
            value <- cursor.get[Json](Key.Value)
            children <- value.hcursor.get[List[Template]](Key.Children)
            href <- value.hcursor.get[Attribute](Key.Href)
          yield Template.Link(children, href)
        case Key.Space => Right(Template.Space)
        case Key.Text =>
          for
            value <- cursor.get[Json](Key.Value)
            children <- value.hcursor.get[List[Value]](Key.Children)
          yield Template.Text(children)
        case tpe => Left(DecodingFailure(s"Unknown: '$tpe'", cursor.downField(Key.Type).history))

  given Encoder.AsObject[Template] =
    case Template.Block(children, paragraph) =>
      JsonObject(
        Key.Type := Key.Block,
        Key.Value := JsonObject(Key.Children := children.asJson, Key.Paragraph := paragraph)
      )
    case Template.Button(children, href) =>
      JsonObject(
        Key.Type := Key.Button,
        Key.Value := JsonObject(Key.Children := children.asJson, Key.Href := href.asJson)
      )
    case Template.Headline(children) =>
      JsonObject(Key.Type := Key.Headline, Key.Value := JsonObject(Key.Children := children))
    case Template.Linebreak => JsonObject(Key.Type := Key.Linebreak)
    case Template.Link(children, href) =>
      JsonObject(
        Key.Type := Key.Link,
        Key.Value := JsonObject(Key.Children := children.asJson, Key.Href := href.asJson)
      )
    case Template.Space          => JsonObject(Key.Type := Key.Space)
    case Template.Text(children) => JsonObject(Key.Type := Key.Text, Key.Value := JsonObject(Key.Children := children))

  given Decoder[Message] = cursor =>
    for
      title <- cursor.get[String](Key.Title)
      preheader <- cursor.get[Option[String]](Key.Preheader)
      body <- cursor.get[List[Template]](Key.Body)
    yield Message(title, preheader, body)

  given Encoder.AsObject[Message] = message =>
    JsonObject(Key.Title := message.title, Key.Preheader := message.preheader, Key.Body := message.body)
