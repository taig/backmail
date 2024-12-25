package io.taig.backmail

import io.taig.backmail.dsl.*
import munit.FunSuite

final class DebugPrinterTest extends FunSuite:
  val sample = message(title = "Title")(
    headline(text("Title")),
    block(
      paragraph(plain("Plaintext"), plain(" "), secret("Secret")),
      paragraph(truncated = true)(plain("Lorem ipusm dolar sit amet."))
    ),
    block(
      button(href = attr(plain("?token="), secret("foobar")))(text("Confirm email"))
    ),
    block(
      link(href = attr(plain("?token="), secret("foobar")))(text("Hyperlink"))
    ),
    alert(text("Lorem ipsum dolar sit amet."))
  )

  test("print"):
    val expected =
      """Title
        |
        |Plaintext ******
        |
        |Lorem ipusm dolar sit amet.
        |
        |Confirm email: ?token=******
        |
        |Hyperlink (?token=******)
        |
        |***
        |Lorem ipsum dolar sit amet.
        |***""".stripMargin

    assertEquals(obtained = DebugPrinter.print(sample), expected)
