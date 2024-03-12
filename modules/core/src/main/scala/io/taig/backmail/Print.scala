package io.taig.backmail

abstract class Printer:
  def print(email: Email): String
