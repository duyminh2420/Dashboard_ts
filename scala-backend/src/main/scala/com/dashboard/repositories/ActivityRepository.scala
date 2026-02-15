package com.dashboard.repositories

import com.dashboard.models.Activity
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait ActivityRepository {
  def getAll(): Future[List[Activity]]
  def getById(id: Int): Future[Option[Activity]]
  def getByUserId(userId: Int): Future[List[Activity]]
  def create(activity: Activity): Future[Activity]
  def delete(id: Int): Future[Boolean]
}

class InMemoryActivityRepository extends ActivityRepository {

  private var activities: List[Activity] = List(
    Activity(1, 1, "purchased Playstation 5 Digital Edition", "3 day ago"),
    Activity(2, 1, "added 3 items into their wishlist", "1 week ago"),
    Activity(3, 1, "purchased Sony Bravia KD-32w800", "2 weeks ago"),
    Activity(4, 2, "purchased iPhone 13 Pro Max", "1 day ago"),
    Activity(5, 2, "added 2 items into their wishlist", "3 weeks ago"),
    Activity(6, 3, "purchased Samsung Galaxy S22", "2 days ago"),
  )

  override def getAll(): Future[List[Activity]] = Future.successful(activities)

  override def getById(id: Int): Future[Option[Activity]] = 
    Future.successful(activities.find(_.id == id))

  override def getByUserId(userId: Int): Future[List[Activity]] = 
    Future.successful(activities.filter(_.userId == userId))

  override def create(activity: Activity): Future[Activity] = {
    val newId = activities.map(_.id).maxOption.getOrElse(0) + 1
    val newActivity = activity.copy(id = newId)
    activities = activities :+ newActivity
    Future.successful(newActivity)
  }

  override def delete(id: Int): Future[Boolean] = {
    val before = activities.length
    activities = activities.filterNot(_.id == id)
    Future.successful(activities.length < before)
  }
}
