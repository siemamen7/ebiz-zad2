package models

import play.api.libs.json._

case class CartItem(product: Product, quantity: Int)
object CartItem {
  implicit val format: OFormat[CartItem] = Json.format[CartItem]
}

case class Cart(items: Seq[CartItem], total: Double)
object Cart {
  implicit val format: OFormat[Cart] = Json.format[Cart]
}

case class CartUpdateRequest(productId: Int, quantity: Int)
object CartUpdateRequest {
  implicit val format: OFormat[CartUpdateRequest] = Json.format[CartUpdateRequest]
}
