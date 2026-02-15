package com.dashboard

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging
import com.dashboard.routes.{UserRoutes, ProductRoutes, ActivityRoutes}
import com.dashboard.repositories.{InMemoryUserRepository, InMemoryProductRepository, InMemoryActivityRepository}
import com.dashboard.services.{UserService, ProductService, ActivityService}
import scala.util.{Success, Failure}
import scala.io.StdIn

object Main extends LazyLogging {
  
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "dashboard-backend")
    implicit val executionContext = system.executionContext

    // Initialize repositories
    val userRepository = new InMemoryUserRepository
    val productRepository = new InMemoryProductRepository
    val activityRepository = new InMemoryActivityRepository

    // Initialize services
    val userService = new UserService(userRepository)
    val productService = new ProductService(productRepository)
    val activityService = new ActivityService(activityRepository)

    // Initialize routes
    val userRoutes = new UserRoutes(userService)
    val productRoutes = new ProductRoutes(productService)
    val activityRoutes = new ActivityRoutes(activityService)

    // Combine all routes
    val routes: Route = cors() {
      concat(
        userRoutes.routes,
        productRoutes.routes,
        activityRoutes.routes,
        // Health check endpoint
        path("api" / "health") {
          get {
            complete("Dashboard backend is running!")
          }
        }
      )
    }

    // Start HTTP server
    val host = "0.0.0.0"
    val port = 8080

    val serverBinding = Http()
      .newServerAt(host, port)
      .bind(routes)
      .map { binding =>
        logger.info(s"Dashboard backend started at http://$host:$port")
        logger.info("Press ENTER to stop the server...")
        binding
      }

    serverBinding.onComplete {
      case Success(binding) =>
        StdIn.readLine()
        binding.unbind().onComplete(_ => system.terminate())
      case Failure(ex) =>
        logger.error("Failed to start server", ex)
        system.terminate()
    }
  }
}
