package com.dashboard.services

import com.dashboard.models.Activity
import com.dashboard.repositories.ActivityRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ActivityService(activityRepository: ActivityRepository) {

  def getAllActivities(): Future[List[Activity]] = 
    activityRepository.getAll()

  def getActivityById(id: Int): Future[Option[Activity]] = {
    if (id <= 0) {
      Future.failed(new IllegalArgumentException("Invalid activity ID"))
    } else {
      activityRepository.getById(id)
    }
  }

  def getUserActivities(userId: Int): Future[List[Activity]] = {
    if (userId <= 0) {
      Future.failed(new IllegalArgumentException("Invalid user ID"))
    } else {
      activityRepository.getByUserId(userId)
    }
  }

  def createActivity(activity: Activity): Future[Activity] = {
    if (activity.text.trim.isEmpty || activity.userId <= 0) {
      Future.failed(new IllegalArgumentException("Text and valid UserId are required"))
    } else {
      activityRepository.create(activity)
    }
  }

  def deleteActivity(id: Int): Future[Boolean] = 
    activityRepository.delete(id)

  def getRecentActivities(limit: Int = 10): Future[List[Activity]] = {
    activityRepository.getAll().map(_.takeRight(limit).reverse)
  }

  def getUserActivityCount(userId: Int): Future[Int] = {
    getUserActivities(userId).map(_.length)
  }
}
