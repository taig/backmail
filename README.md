# Backmail

> Email templating utilities

## Motivation

Creating beautiful transactional HTML emails is usually a horrible experience. So we tend to resort to the HTML templating feature of our transactional email providers. But this comes with a price: we are getting vendor-locked, argument substituion is wonky and i18n becomes an even more difficult challenge than it already is. This library provides the fundamental building blocks to manage HTML emails in the backend.

## Installation

```sbt
libraryDependencies ++=
  "io.taig" %% "backmail-core" % "x.y.z" ::
  "io.taig" %% "backmail-circe" % "x.y.z" ::
  Nil
```

## Usage

This library provides data structures to describe emails. The provided printers can then be used to turn the email descriptions into plaintext emails, HTML emails or debug messages that hide secret values.

### Define an email

```scala
import io.taig.backmail.dsl.*

val myEmail = email(title = "Title")(
  headline(text("Title")),
  block(text(plain("Plaintext"), plain(" "), secret("Secret"))),
  block(paragraph = false)(text("Lorem ipusm dolar sit amet."), linebreak, text("Lorem ipusm dolar sit amet.")),
  space,
  button(href = attr(plain("?token="), secret("foobar")))(text("Confirm email")),
  space,
  block(paragraph = false)(text("Lorem ipusm dolar sit amet."))
)
```

### Print as plaintext

An HTML email should always have a plaintext fallback. This printer is intended to provide this email variant.

```scala
import io.taig.backmail.*
PlaintextPrinter.print(myEmail)

val res0: String = Title

Plaintext Secret

Lorem ipusm dolar sit amet.
Lorem ipusm dolar sit amet.

Confirm email: ?token=foobar

Lorem ipusm dolar sit amet.
```

### Print as debug message

Print the email as a plaintext message with hidden secret values so it is safe to log.

```scala
import io.taig.backmail.*
DebugPrinter.print(myEmail)

val res1: String = Title

Plaintext ******

Lorem ipusm dolar sit amet.
Lorem ipusm dolar sit amet.

Confirm email: ?token=******

Lorem ipusm dolar sit amet.
```

### Print as HTML

Now this is where things get interesting:

1. Start by picking an HTML email templating generator of your choice (e.g. [Maizzle](https://maizzle.com) or [Bootstrap Email](https://bootstrapemail.com))
2. Create a template that includes a headline, a button and paragraphs of text
3. Use the generated HTML to implement your own `HtmlPrinter`

## Disclaimer

This library was created to meet my personal needs and may lack features that are important to you. Contributions welcome (-:
