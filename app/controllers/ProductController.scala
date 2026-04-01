package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import models.Product

@Singleton
class ProductController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  // List of products
  private var products = List(
    Product(1, "Wierło głębinowe", 3000),
    Product(2, "Myjka ciśnieniowa", 500),
    Product(3, "Suszarka spalinowa", 1500),
    Product(4, "Separator iniekcyjny", 2000)
  )

  // GET /products
  def getAll() = Action {
    Ok(Json.toJson(products))
  }

  // GET /products/:id
  def getById(id: Int) = Action {
    products.find(_.id == id) match {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound(Json.obj("error" -> "Product not found"))
    }
  }

  // POST /products
  def create = Action(parse.json) { request =>
    request.body.validate[Product].fold(
      errors => BadRequest(Json.obj("error" -> "Invalid JSON")),
      product => {
        products = products :+ product
        Created(Json.toJson(product))
      }
    )
  }

  // PUT /products/:id
  def update(id: Int) = Action(parse.json) { request =>
    request.body.validate[Product].fold(
      errors => BadRequest(Json.obj("error" -> "Invalid JSON")),
      updatedProduct => {
        products = products.map {
          case p if p.id == id => updatedProduct
          case p => p
        }
        Ok(Json.toJson(updatedProduct))
      }
    )
  }

  // DELETE /products/:id
  def delete(id: Int) = Action {
    products = products.filterNot(_.id == id)
    Ok(Json.obj("message" -> "Deleted"))
  }
}

