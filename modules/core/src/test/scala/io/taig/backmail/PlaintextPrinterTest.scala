package io.taig.backmail

import io.taig.backmail.dsl.*
import munit.FunSuite

final class PlaintextPrinterTest extends FunSuite:
  val sample = message(title = "Title")(
    headline(text("Title")),
    block(text(plain("Plaintext"), plain(" "), secret("Secret"))),
    block(paragraph = false)(text("Lorem ipusm dolar sit amet."), linebreak, text("Lorem ipusm dolar sit amet.")),
    space,
    button(href = attr(plain("?token="), secret("foobar")))(text("Confirm email")),
    space,
    block(paragraph = true)(
      link(href = attr(plain("?token="), secret("foobar")))(text("Hyperlink"))
    ),
    block(paragraph = false)(text("Lorem ipusm dolar sit amet.")),
    space,
    alert(text("Lorem ipsum dolar sit amet."))
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
        |Hyperlink (?token=foobar)
        |
        |Lorem ipusm dolar sit amet.
        |
        |***
        |Lorem ipsum dolar sit amet.
        |***""".stripMargin

    assertEquals(obtained = PlaintextPrinter.print(sample), expected)
