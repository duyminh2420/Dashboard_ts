package com.dashboard.repositories

import com.dashboard.models.User
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Repository pattern for User data access
 * In production, this would interface with a real database using ScalikeJDBC
 */
trait UserRepository {
  def getAll(): Future[List[User]]
  def getById(id: Int): Future[Option[User]]
  def create(user: User): Future[User]
  def update(id: Int, user: User): Future[Option[User]]
  def delete(id: Int): Future[Boolean]
  def search(query: String): Future[List[User]]
}

class InMemoryUserRepository extends UserRepository {
  
  private var users: List[User] = List(
    User(1, "Eula", "Hubbard", "kewez@gmail.com", "123 456 789", 
      "https://images.pexels.com/photos/8405873/pexels-photo-8405873.jpeg?auto=compress&cs=tinysrgb&w=1600&lazy=load", "01.02.2023", verified = true),
    User(2, "Stella", "Manning", "comhuhmit@gmail.com", "123 456 789",
      "https://images.pexels.com/photos/1181519/pexels-photo-1181519.jpeg?auto=compress&cs=tinysrgb&w=1600", "01.02.2023", verified = true),
    User(3, "Mary", "Greer", "ujudokon@hottmail.com", "123 456 789",
      "https://images.pexels.com/photos/1587009/pexels-photo-1587009.jpeg?auto=compress&cs=tinysrgb&w=1600", "01.02.2023", verified = true),
  )

  override def getAll(): Future[List[User]] = Future.successful(users)

  override def getById(id: Int): Future[Option[User]] = 
    Future.successful(users.find(_.id == id))

  override def create(user: User): Future[User] = {
    val newId = users.map(_.id).maxOption.getOrElse(0) + 1
    val newUser = user.copy(id = newId)
    users = users :+ newUser
    Future.successful(newUser)
  }

  override def update(id: Int, user: User): Future[Option[User]] = {
    val index = users.indexWhere(_.id == id)
    if (index >= 0) {
      val updated = user.copy(id = id)
      users = users.updated(index, updated)
      Future.successful(Some(updated))
    } else {
      Future.successful(None)
    }
  }

  override def delete(id: Int): Future[Boolean] = {
    val before = users.length
    users = users.filterNot(_.id == id)
    Future.successful(users.length < before)
  }

  override def search(query: String): Future[List[User]] = 
    Future.successful(
      users.filter(u => 
        u.firstName.toLowerCase.contains(query.toLowerCase) ||
        u.lastName.toLowerCase.contains(query.toLowerCase) ||
        u.email.toLowerCase.contains(query.toLowerCase)
      )
    )
}

/**
 * PostgreSQL Repository (for production use)
 * Requires database connection and schema setup
 */
class PostgresUserRepository extends UserRepository {
  // TODO: Implement with ScalikeJDBC
  override def getAll(): Future[List[User]] = ???
  override def getById(id: Int): Future[Option[User]] = ???
  override def create(user: User): Future[User] = ???
  override def update(id: Int, user: User): Future[Option[User]] = ???
  override def delete(id: Int): Future[Boolean] = ???
  override def search(query: String): Future[List[User]] = ???
}
