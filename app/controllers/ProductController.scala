package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import models.Product
import services.StoreService

@Singleton
class ProductController @Inject()(val controllerComponents: ControllerComponents, store: StoreService)
    extends BaseController {

  def getAll() = Action {
    Ok(Json.toJson(store.listProducts()))
  }

  def getById(id: Int) = Action {
    store.getProduct(id) match {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound(Json.obj("error" -> "Product not found"))
    }
  }

  def create = Action(parse.json) { request =>
    request.body.validate[Product].fold(
      errors => BadRequest(Json.obj("error" -> "Invalid JSON")),
      product => {
        store.createProduct(product)
        Created(Json.toJson(product))
      }
    )
  }

  def update(id: Int) = Action(parse.json) { request =>
    request.body.validate[Product].fold(
      errors => BadRequest(Json.obj("error" -> "Invalid JSON")),
      updatedProduct => {
        store.updateProduct(id, updatedProduct) match {
          case Some(product) => Ok(Json.toJson(product))
          case None => NotFound(Json.obj("error" -> "Product not found"))
        }
      }
    )
  }

  def delete(id: Int) = Action {
    if (store.deleteProduct(id)) {
      NoContent
    } else {
      NotFound(Json.obj("error" -> "Product not found"))
    }
  }
}

