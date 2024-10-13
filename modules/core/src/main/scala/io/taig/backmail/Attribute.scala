package io.taig.backmail

opaque type Attribute = List[Value]

object Attribute:
  extension (self: Attribute) inline def toList: List[Value] = self
  inline def apply(values: List[Value]): Attribute = values
