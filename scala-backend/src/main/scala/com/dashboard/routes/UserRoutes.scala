package com.dashboard.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import com.dashboard.models.{User, ApiResponse}
import com.dashboard.services.UserService
import scala.util.{Success, Failure}

class UserRoutes(userService: UserService) {

  val routes: Route = 
    pathPrefix("api" / "users") {
      // GET /api/users - Get all users
      get {
        path(Segment.?) {
          case None =>
            parameters("search".?, "page".as[Int].?, "limit".as[Int].?) { (search, page, limit) =>
              val searchQuery = search.getOrElse("")
              onComplete(userService.searchUsers(searchQuery)) {
                case Success(users) =>
                  complete(StatusCodes.OK -> ApiResponse.success(users))
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError -> ApiResponse.error[List[User]](ex.getMessage))
              }
            }
          // GET /api/users/:id - Get user by ID
          case Some(userId) =>
            val id = userId.toIntOption
            id match {
              case Some(uid) =>
                onComplete(userService.getUserById(uid)) {
                  case Success(Some(user)) =>
                    complete(StatusCodes.OK -> ApiResponse.success(user))
                  case Success(None) =>
                    complete(StatusCodes.NotFound -> ApiResponse.error[User]("User not found"))
                  case Failure(ex) =>
                    complete(StatusCodes.InternalServerError -> ApiResponse.error[User](ex.getMessage))
                }
              case None =>
                complete(StatusCodes.BadRequest -> ApiResponse.error[User]("Invalid user ID"))
            }
        }
      } ~
      // POST /api/users - Create new user
      post {
        entity(as[User]) { user =>
          onComplete(userService.createUser(user)) {
            case Success(createdUser) =>
              complete(StatusCodes.Created -> ApiResponse.success(createdUser))
            case Failure(ex) =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[User](ex.getMessage))
          }
        }
      } ~
      // PUT /api/users/:id - Update user
      put {
        path(Segment) { userId =>
          val id = userId.toIntOption
          id match {
            case Some(uid) =>
              entity(as[User]) { user =>
                onComplete(userService.updateUser(uid, user)) {
                  case Success(Some(updatedUser)) =>
                    complete(StatusCodes.OK -> ApiResponse.success(updatedUser))
                  case Success(None) =>
                    complete(StatusCodes.NotFound -> ApiResponse.error[User]("User not found"))
                  case Failure(ex) =>
                    complete(StatusCodes.BadRequest -> ApiResponse.error[User](ex.getMessage))
                }
              }
            case None =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[User]("Invalid user ID"))
          }
        }
      } ~
      // DELETE /api/users/:id - Delete user
      delete {
        path(Segment) { userId =>
          val id = userId.toIntOption
          id match {
            case Some(uid) =>
              onComplete(userService.deleteUser(uid)) {
                case Success(true) =>
                  complete(StatusCodes.OK -> ApiResponse.success[String]("User deleted successfully"))
                case Success(false) =>
                  complete(StatusCodes.NotFound -> ApiResponse.error[String]("User not found"))
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError -> ApiResponse.error[String](ex.getMessage))
              }
            case None =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[String]("Invalid user ID"))
          }
        }
      } ~
      // GET /api/users/stats/overview - Get user statistics
      path("stats" / "overview") {
        get {
          onComplete(userService.getUserStatistics()) {
            case Success(stats) =>
              complete(StatusCodes.OK -> ApiResponse.success(stats))
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError -> ApiResponse.error[Map[String, Int]](ex.getMessage))
          }
        }
      }
    }
}
