package io.taig.backmail

import munit.FunSuite
import io.taig.backmail.dsl.*

final class PlaintextPrinterTest extends FunSuite:
  val sample = email(title = "Title")(
    headline(text("Title")),
    block(text(plain("Plaintext"), plain(" "), secret("Secret"))),
    block(paragraph = false)(text("Lorem ipusm dolar sit amet."), linebreak, text("Lorem ipusm dolar sit amet.")),
    space,
    button(href = attr(plain("?token="), secret("foobar")))(text("Confirm email")),
    space,
    block(paragraph = false)(text("Lorem ipusm dolar sit amet."))
  )

  test("print"):
    val expected =
      """Title
        |
        |Plaintext Secret
        |
        |Lorem ipusm dolar sit amet.
        |Lorem ipusm dolar sit amet.
        |
        |Confirm email: ?token=foobar
        |
        |Lorem ipusm dolar sit amet.""".stripMargin

    assertEquals(obtained = PlaintextPrinter.print(sample), expected)
