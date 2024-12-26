package io.taig.backmail

import io.taig.backmail.dsl.*
import munit.FunSuite

final class DebugPrinterTest extends FunSuite:
  val sample = message(title = "Title")(
    section(
      headline(text("Title")),
      paragraph("Plaintext", " ", secret("Secret")),
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
        |Plaintext ******
        |
        |Lorem ipusm dolar sit amet. Hyperlink (?token=******)
        |
        |Confirm email: ?token=******
        |
        |
        |***
        |Lorem ipsum dolar sit amet.
        |***""".stripMargin

    assertEquals(obtained = DebugPrinter.print(sample), expected)
