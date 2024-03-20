package io.taig.backmail

abstract class Printer:
  def print(email: Message): String
