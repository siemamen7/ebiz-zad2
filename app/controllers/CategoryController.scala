package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import models.Category
import services.StoreService

@Singleton
class CategoryController @Inject()(val controllerComponents: ControllerComponents, store: StoreService)
    extends BaseController {

  def getAll(): Action[AnyContent] = Action {
    Ok(Json.toJson(store.listCategories()))
  }

  def getById(id: Long): Action[AnyContent] = Action {
    store.getCategory(id) match {
      case Some(cat) => Ok(Json.toJson(cat))
      case None => NotFound(Json.obj("error" -> "Category not found"))
    }
  }

  def getProductsByCategory(id: Long): Action[AnyContent] = Action {
    store.productsByCategory(id) match {
      case Some(products) => Ok(Json.toJson(products))
      case None => NotFound(Json.obj("error" -> "Category not found"))
    }
  }

  def create(): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Category].fold(
      errors => BadRequest(Json.obj("error" -> "Invalid JSON")),
      category => {
        store.createCategory(category)
        Created(Json.toJson(category))
      }
    )
  }

  def update(id: Long): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Category].fold(
      errors => BadRequest(Json.obj("error" -> "Invalid JSON")),
      updatedCategory => {
        store.updateCategory(id, updatedCategory) match {
          case Some(category) => Ok(Json.toJson(category))
          case None => NotFound(Json.obj("error" -> "Category not found"))
        }
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action {
    if (store.deleteCategory(id)) {
      NoContent
    } else {
      NotFound(Json.obj("error" -> "Category not found"))
    }
  }
}
