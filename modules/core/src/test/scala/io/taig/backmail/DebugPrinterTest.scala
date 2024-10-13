package io.taig.backmail

import io.taig.backmail.dsl.*
import munit.FunSuite

final class DebugPrinterTest extends FunSuite:
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

  test("print"):
    val expected =
      """Title
        |
        |Plaintext ******
        |
        |Lorem ipusm dolar sit amet.
        |Lorem ipusm dolar sit amet.
        |
        |Confirm email: ?token=******
        |
        |Hyperlink: ?token=******
        |
        |Lorem ipusm dolar sit amet.""".stripMargin

    assertEquals(obtained = DebugPrinter.print(sample), expected)
