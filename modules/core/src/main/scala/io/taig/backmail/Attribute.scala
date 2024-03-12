package io.taig.backmail

opaque type Attribute = List[Value]

object Attribute:
  extension (self: Attribute) def toList: List[Value] = self
  def apply(values: List[Value]): Attribute = values
