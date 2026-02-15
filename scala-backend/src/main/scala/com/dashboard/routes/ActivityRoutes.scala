package com.dashboard.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import com.dashboard.models.{Activity, ApiResponse}
import com.dashboard.services.ActivityService
import scala.util.{Success, Failure}

class ActivityRoutes(activityService: ActivityService) {

  val routes: Route = 
    pathPrefix("api" / "activities") {
      // GET /api/activities - Get all activities
      get {
        path(Segment.?) {
          case None =>
            parameters("recent".as[Int].?) { recent =>
              val limit = recent.getOrElse(10)
              onComplete(activityService.getRecentActivities(limit)) {
                case Success(activities) =>
                  complete(StatusCodes.OK -> ApiResponse.success(activities))
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError -> ApiResponse.error[List[Activity]](ex.getMessage))
              }
            }
          // GET /api/activities/:id - Get activity by ID
          case Some(activityId) =>
            val id = activityId.toIntOption
            id match {
              case Some(aid) =>
                onComplete(activityService.getActivityById(aid)) {
                  case Success(Some(activity)) =>
                    complete(StatusCodes.OK -> ApiResponse.success(activity))
                  case Success(None) =>
                    complete(StatusCodes.NotFound -> ApiResponse.error[Activity]("Activity not found"))
                  case Failure(ex) =>
                    complete(StatusCodes.InternalServerError -> ApiResponse.error[Activity](ex.getMessage))
                }
              case None =>
                complete(StatusCodes.BadRequest -> ApiResponse.error[Activity]("Invalid activity ID"))
            }
        }
      } ~
      // POST /api/activities - Create new activity
      post {
        entity(as[Activity]) { activity =>
          onComplete(activityService.createActivity(activity)) {
            case Success(createdActivity) =>
              complete(StatusCodes.Created -> ApiResponse.success(createdActivity))
            case Failure(ex) =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[Activity](ex.getMessage))
          }
        }
      } ~
      // DELETE /api/activities/:id - Delete activity
      delete {
        path(Segment) { activityId =>
          val id = activityId.toIntOption
          id match {
            case Some(aid) =>
              onComplete(activityService.deleteActivity(aid)) {
                case Success(true) =>
                  complete(StatusCodes.OK -> ApiResponse.success[String]("Activity deleted successfully"))
                case Success(false) =>
                  complete(StatusCodes.NotFound -> ApiResponse.error[String]("Activity not found"))
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError -> ApiResponse.error[String](ex.getMessage))
              }
            case None =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[String]("Invalid activity ID"))
          }
        }
      } ~
      // GET /api/activities/user/:userId - Get activities by user
      path("user" / Segment) { userId =>
        get {
          val id = userId.toIntOption
          id match {
            case Some(uid) =>
              onComplete(activityService.getUserActivities(uid)) {
                case Success(activities) =>
                  complete(StatusCodes.OK -> ApiResponse.success(activities))
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError -> ApiResponse.error[List[Activity]](ex.getMessage))
              }
            case None =>
              complete(StatusCodes.BadRequest -> ApiResponse.error[List[Activity]]("Invalid user ID"))
          }
        }
      }
    }
}
