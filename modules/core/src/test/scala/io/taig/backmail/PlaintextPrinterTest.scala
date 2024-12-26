package io.taig.backmail

import io.taig.backmail.dsl.*
import munit.FunSuite

final class PlaintextPrinterTest extends FunSuite:
  val sample = message(title = "Title")(
    section(
      headline(text("Title")),
      paragraph(plain("Plaintext"), plain(" "), secret("Secret")),
      paragraph(
        plain("Lorem ipusm dolar sit amet. "),
        link(href = attr(plain("?token="), secret("foobar")))("Hyperlink")
      ),
      button(href = attr(plain("?token="), secret("foobar")))(text("Confirm email"))
    ),
    alert(text("Lorem ipsum dolar sit amet."))
  )

  test("print"):
    val expected =
      """Title
        |
        |Plaintext Secret
        |
        |Lorem ipusm dolar sit amet. Hyperlink (?token=foobar)
        |
        |Confirm email: ?token=foobar
        |
        |
        |***
        |Lorem ipsum dolar sit amet.
        |***""".stripMargin

    assertEquals(obtained = PlaintextPrinter.print(sample), expected)
