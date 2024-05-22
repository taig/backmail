package io.taig.backmail

import munit.FunSuite
import io.circe.syntax.*
import io.taig.backmail.dsl.*
import io.taig.backmail.circe.given

final class CirceCodecTest extends FunSuite:
  val sample = message(title = "Title")(
    headline(text("Title")),
    block(text(plain("Plaintext"), plain(" "), secret("Secret"))),
    block(paragraph = false)(text("Lorem ipusm dolar sit amet."), linebreak, text("Lorem ipusm dolar sit amet.")),
    space,
    button(href = attr(plain("?token="), secret("foobar")))(text("Confirm email")),
    space,
    block(paragraph = false)(
      link(href = attr(plain("?token="), secret("foobar")))(text("Hyperlink"))
    ),
    space,
    block(paragraph = false)(text("Lorem ipusm dolar sit amet."))
  )

  test("round-trip"):
    assertEquals(obtained = sample.asJson.as[Message], expected = Right(sample))
