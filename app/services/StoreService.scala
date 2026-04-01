package services

import javax.inject.Singleton

import models.{Cart, CartItem, Category, Product}

@Singleton
class StoreService {

  private var products = List(
    Product(1, "Wiertlo glebinowe", 3000, "Kopalniane"),
    Product(2, "Myjka cisnieniowa", 500, "Czyszczenie"),
    Product(3, "Suszarka spalinowa", 1500, "Inne"),
    Product(4, "Separator iniekcyjny", 2000, "Inne"),
    Product(5, "Wiertlo klinowe", 2500, "Kopalniane")
  )

  private var categories = List(
    Category(1, "Kopalniane"),
    Category(2, "Czyszczenie"),
    Category(3, "Inne")
  )

  private var cartItems = List.empty[CartItem]

  def listProducts(): List[Product] = products

  def getProduct(id: Int): Option[Product] = products.find(_.id == id)

  def createProduct(product: Product): Product = {
    products = products :+ product
    product
  }

  def updateProduct(id: Int, updatedProduct: Product): Option[Product] = {
    if (products.exists(_.id == id)) {
      products = products.map {
        case p if p.id == id => updatedProduct
        case p => p
      }
      Some(updatedProduct)
    } else {
      None
    }
  }

  def deleteProduct(id: Int): Boolean = {
    val before = products.size
    products = products.filterNot(_.id == id)
    before != products.size
  }

  def listCategories(): List[Category] = categories

  def getCategory(id: Long): Option[Category] = categories.find(_.id == id)

  def createCategory(category: Category): Category = {
    categories = categories :+ category
    category
  }

  def updateCategory(id: Long, updatedCategory: Category): Option[Category] = {
    if (categories.exists(_.id == id)) {
      categories = categories.map {
        case c if c.id == id => updatedCategory
        case c => c
      }
      Some(updatedCategory)
    } else {
      None
    }
  }

  def deleteCategory(id: Long): Boolean = {
    val before = categories.size
    categories = categories.filterNot(_.id == id)
    before != categories.size
  }

  def productsByCategory(id: Long): Option[List[Product]] = {
    getCategory(id).map { category =>
      products.filter(_.category == category.name)
    }
  }

  def getCart: Cart = {
    val total = cartItems.map(item => item.product.price * item.quantity.toDouble).sum
    Cart(cartItems, total)
  }

  def addToCart(productId: Int, quantity: Int): Option[CartItem] = {
    getProduct(productId).map { product =>
      cartItems = cartItems match {
        case items if items.exists(_.product.id == productId) =>
          items.map {
            case item if item.product.id == productId =>
              item.copy(quantity = item.quantity + quantity)
            case item => item
          }
        case items => items :+ CartItem(product, quantity)
      }
      cartItems.find(_.product.id == productId).get
    }
  }

  def updateCartItem(productId: Int, quantity: Int): Option[CartItem] = {
    cartItems.find(_.product.id == productId).flatMap { existing =>
      if (quantity <= 0) {
        cartItems = cartItems.filterNot(_.product.id == productId)
        None
      } else {
        val updated = existing.copy(quantity = quantity)
        cartItems = cartItems.map {
          case item if item.product.id == productId => updated
          case item => item
        }
        Some(updated)
      }
    }
  }

  def removeCartItem(productId: Int): Boolean = {
    val before = cartItems.size
    cartItems = cartItems.filterNot(_.product.id == productId)
    before != cartItems.size
  }

  def clearCart(): Unit = {
    cartItems = Nil
  }
}
