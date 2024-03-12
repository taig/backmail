package io.taig.backmail

enum Value:
  case Plain(value: String)
  case Secret(value: String)
