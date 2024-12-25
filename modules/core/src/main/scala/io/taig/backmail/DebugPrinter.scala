package io.taig.backmail

object DebugPrinter extends StringPrinter:
  override def print(value: Value): String = value match
    case Value.Plain(value)  => value
    case Value.Secret(value) => "*" * value.length
