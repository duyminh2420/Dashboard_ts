package com.dashboard.services

import com.dashboard.models.User
import com.dashboard.repositories.UserRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Service layer for User business logic
 * Orchestrates between API routes and repository layer
 */
class UserService(userRepository: UserRepository) {

  def getAllUsers(): Future[List[User]] = 
    userRepository.getAll()

  def getUserById(id: Int): Future[Option[User]] = 
    if (id <= 0) {
      Future.failed(new IllegalArgumentException("Invalid user ID"))
    } else {
      userRepository.getById(id)
    }

  def createUser(user: User): Future[User] = {
    if (user.email.trim.isEmpty || user.firstName.trim.isEmpty) {
      Future.failed(new IllegalArgumentException("Email and First Name are required"))
    } else if (!isValidEmail(user.email)) {
      Future.failed(new IllegalArgumentException("Invalid email format"))
    } else {
      userRepository.create(user)
    }
  }

  def updateUser(id: Int, user: User): Future[Option[User]] = {
    if (!isValidEmail(user.email)) {
      Future.failed(new IllegalArgumentException("Invalid email format"))
    } else {
      userRepository.update(id, user)
    }
  }

  def deleteUser(id: Int): Future[Boolean] = 
    userRepository.delete(id)

  def searchUsers(query: String): Future[List[User]] = {
    if (query.trim.isEmpty) {
      getAllUsers()
    } else {
      userRepository.search(query)
    }
  }

  def getUserStatistics(): Future[Map[String, Int]] = {
    userRepository.getAll().map { users =>
      Map(
        "total" -> users.length,
        "verified" -> users.count(_.verified),
        "unverified" -> users.count(!_.verified)
      )
    }
  }

  private def isValidEmail(email: String): Boolean = 
    """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""".r.matches(email)
}
