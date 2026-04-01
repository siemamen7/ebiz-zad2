package models

import play.api.libs.json._

case class Product(id: Int, name: String, price: Double, category: String)

object Product {
  implicit val productFormat: OFormat[Product] = Json.format[Product]
}
