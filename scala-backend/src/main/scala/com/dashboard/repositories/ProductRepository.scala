package com.dashboard.repositories

import com.dashboard.models.Product
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait ProductRepository {
  def getAll(): Future[List[Product]]
  def getById(id: Int): Future[Option[Product]]
  def create(product: Product): Future[Product]
  def update(id: Int, product: Product): Future[Option[Product]]
  def delete(id: Int): Future[Boolean]
  def getInStock(): Future[List[Product]]
  def search(query: String): Future[List[Product]]
}

class InMemoryProductRepository extends ProductRepository {

  private var products: List[Product] = List(
    Product(1, "Playstation 5 Digital Edition", "white", "Sony", "$250.99",
      "https://store.sony.com.au/on/demandware.static/-/Sites-sony-master-catalog/default/dw1b537bbb/images/PLAYSTATION5W/PLAYSTATION5W.png", "01.02.2023", inStock = true),
    Product(2, "Dell Laptop KR211822", "black", "Dell", "$499.99",
      "https://www.pngmart.com/files/6/Dell-Laptop-PNG-Image.png", "01.02.2023", inStock = true),
    Product(3, "Samsung TV 4K SmartTV", "gray", "Samsung", "$999.49",
      "http://images.samsung.com/is/image/samsung/uk-led-tv-hg40ed670ck-hg40ed670ckxxu-001-front", "01.02.2023", inStock = true),
  )

  override def getAll(): Future[List[Product]] = Future.successful(products)

  override def getById(id: Int): Future[Option[Product]] = 
    Future.successful(products.find(_.id == id))

  override def create(product: Product): Future[Product] = {
    val newId = products.map(_.id).maxOption.getOrElse(0) + 1
    val newProduct = product.copy(id = newId)
    products = products :+ newProduct
    Future.successful(newProduct)
  }

  override def update(id: Int, product: Product): Future[Option[Product]] = {
    val index = products.indexWhere(_.id == id)
    if (index >= 0) {
      val updated = product.copy(id = id)
      products = products.updated(index, updated)
      Future.successful(Some(updated))
    } else {
      Future.successful(None)
    }
  }

  override def delete(id: Int): Future[Boolean] = {
    val before = products.length
    products = products.filterNot(_.id == id)
    Future.successful(products.length < before)
  }

  override def getInStock(): Future[List[Product]] = 
    Future.successful(products.filter(_.inStock))

  override def search(query: String): Future[List[Product]] = 
    Future.successful(
      products.filter(p => 
        p.title.toLowerCase.contains(query.toLowerCase) ||
        p.producer.toLowerCase.contains(query.toLowerCase)
      )
    )
}
