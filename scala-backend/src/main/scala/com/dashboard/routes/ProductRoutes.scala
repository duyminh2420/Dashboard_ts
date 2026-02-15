package com.dashboard.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import com.dashboard.models.{Product, ApiResponse}
import com.dashboard.services.ProductService
import scala.util.{Success, Failure}

class ProductRoutes(productService: ProductService) {

  val routes: Route = 
    pathPrefix("api" / "products") {
      // GET /api/products - Get all products
      get {
        path(Segment.?) {
          case None =>
            parameters("search".?, "inStock".as[Boolean].?) { (search, inStock) =>
              val searchQuery = search.getOrElse("")
              onComplete(productService.searchProducts(searchQuery)) {
                case Success(products) =>
                  val filtered = if (inStock.contains(true)) {
                    products.filter(_.inStock)
                  } else {
                    products
                  }
                  complete(StatusCodes.OK -> ApiResponse.success(filtered))
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError -> ApiResponse.error[List[Product]](ex.getMessage))
              }
            }
          // GET /api/products/:id - Get product by ID
          case Some(productId) =>
            val id = productId.toIntOption
            id match {
              case Some(pid) =>
                onComplete(productService.getProductById(pid)) {
                  case Success(Some(product)) =>
                    complete(StatusCodes.OK -> ApiResponse.success(product))
                  case Success(None) =>
                    complete(StatusCodes.NotFound -> ApiResponse.error[Product]("Product not found"))
                  case Failure(ex) =>
                    complete(StatusCodes.InternalServerError -> ApiResponse.error[Product](ex.getMessage))
                }
              case None =>
                complete(StatusCodes.BadRequest -> ApiResponse.error[Product]("Invalid product ID"))
            }
        }
      } ~
      // POST /api/products - Create new product
      post {
        entity(as[Product]) { product =>
          onComplete(productService.createProduct(product)) {
            case Success(createdProduct) =>
              complete(StatusCodes.Created -> ApiResponse.success(createdProduct))
            case Failure(ex) =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[Product](ex.getMessage))
          }
        }
      } ~
      // PUT /api/products/:id - Update product
      put {
        path(Segment) { productId =>
          val id = productId.toIntOption
          id match {
            case Some(pid) =>
              entity(as[Product]) { product =>
                onComplete(productService.updateProduct(pid, product)) {
                  case Success(Some(updatedProduct)) =>
                    complete(StatusCodes.OK -> ApiResponse.success(updatedProduct))
                  case Success(None) =>
                    complete(StatusCodes.NotFound -> ApiResponse.error[Product]("Product not found"))
                  case Failure(ex) =>
                    complete(StatusCodes.BadRequest -> ApiResponse.error[Product](ex.getMessage))
                }
              }
            case None =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[Product]("Invalid product ID"))
          }
        }
      } ~
      // DELETE /api/products/:id - Delete product
      delete {
        path(Segment) { productId =>
          val id = productId.toIntOption
          id match {
            case Some(pid) =>
              onComplete(productService.deleteProduct(pid)) {
                case Success(true) =>
                  complete(StatusCodes.OK -> ApiResponse.success[String]("Product deleted successfully"))
                case Success(false) =>
                  complete(StatusCodes.NotFound -> ApiResponse.error[String]("Product not found"))
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError -> ApiResponse.error[String](ex.getMessage))
              }
            case None =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[String]("Invalid product ID"))
          }
        }
      } ~
      // GET /api/products/stats/overview - Get product statistics
      path("stats" / "overview") {
        get {
          onComplete(productService.getProductStatistics()) {
            case Success(stats) =>
              complete(StatusCodes.OK -> ApiResponse.success(stats))
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError -> ApiResponse.error[Map[String, Any]](ex.getMessage))
          }
        }
      }
    }
}
