package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import models.CartUpdateRequest
import services.StoreService

@Singleton
class CartController @Inject()(val controllerComponents: ControllerComponents, store: StoreService)
    extends BaseController {

  def getCart(): Action[AnyContent] = Action {
    Ok(Json.toJson(store.getCart))
  }

  def addItem(): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[CartUpdateRequest].fold(
      errors => BadRequest(Json.obj("error" -> "Invalid JSON")),
      addRequest => {
        store.addToCart(addRequest.productId, addRequest.quantity) match {
          case Some(item) => Created(Json.toJson(item))
          case None => NotFound(Json.obj("error" -> "Product not found"))
        }
      }
    )
  }

  def updateItem(productId: Int): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[CartUpdateRequest].fold(
      errors => BadRequest(Json.obj("error" -> "Invalid JSON")),
      updateRequest => {
        store.updateCartItem(productId, updateRequest.quantity) match {
          case Some(item) => Ok(Json.toJson(item))
          case None => NotFound(Json.obj("error" -> "Cart item not found or removed"))
        }
      }
    )
  }

  def removeItem(productId: Int): Action[AnyContent] = Action {
    if (store.removeCartItem(productId)) {
      Ok(Json.obj("message" -> "Removed from cart"))
    } else {
      NotFound(Json.obj("error" -> "Cart item not found"))
    }
  }

  def clearCart(): Action[AnyContent] = Action {
    store.clearCart()
    Ok(Json.obj("message" -> "Cart cleared"))
  }
}
