package com.dashboard.services

import com.dashboard.models.Product
import com.dashboard.repositories.ProductRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ProductService(productRepository: ProductRepository) {

  def getAllProducts(): Future[List[Product]] = 
    productRepository.getAll()

  def getProductById(id: Int): Future[Option[Product]] = {
    if (id <= 0) {
      Future.failed(new IllegalArgumentException("Invalid product ID"))
    } else {
      productRepository.getById(id)
    }
  }

  def createProduct(product: Product): Future[Product] = {
    if (product.title.trim.isEmpty || product.price.trim.isEmpty) {
      Future.failed(new IllegalArgumentException("Title and Price are required"))
    } else {
      productRepository.create(product)
    }
  }

  def updateProduct(id: Int, product: Product): Future[Option[Product]] = {
    if (product.title.trim.isEmpty) {
      Future.failed(new IllegalArgumentException("Title is required"))
    } else {
      productRepository.update(id, product)
    }
  }

  def deleteProduct(id: Int): Future[Boolean] = 
    productRepository.delete(id)

  def getInStockProducts(): Future[List[Product]] = 
    productRepository.getInStock()

  def searchProducts(query: String): Future[List[Product]] = {
    if (query.trim.isEmpty) {
      getAllProducts()
    } else {
      productRepository.search(query)
    }
  }

  def getProductStatistics(): Future[Map[String, Any]] = {
    productRepository.getAll().map { products =>
      val prices = products
        .flatMap(_.price.replace("$", "").toDoubleOption)
      
      Map(
        "total" -> products.length,
        "inStock" -> products.count(_.inStock),
        "outOfStock" -> products.count(!_.inStock),
        "averagePrice" -> (if (prices.nonEmpty) prices.sum / prices.length else 0),
        "maxPrice" -> (if (prices.nonEmpty) prices.max else 0),
        "minPrice" -> (if (prices.nonEmpty) prices.min else 0)
      )
    }
  }
}
