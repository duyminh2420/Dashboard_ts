package com.dashboard.utils

import scala.concurrent.Future

/**
 * Utility functions for common operations
 */
object ValidationUtils {
  
  /**
   * Validate email format
   */
  def isValidEmail(email: String): Boolean = {
    val emailPattern = """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""".r
    emailPattern.matches(email)
  }

  /**
   * Validate phone number (basic validation)
   */
  def isValidPhone(phone: String): Boolean = {
    phone.replaceAll("[^0-9]", "").length >= 10
  }

  /**
   * Check if string is not null and not empty
   */
  def isNotEmpty(str: String): Boolean = {
    Option(str).exists(_.trim.nonEmpty)
  }

  /**
   * Safely parse integer with fallback
   */
  def parseInt(value: String, default: Int = 0): Int = {
    value.toIntOption.getOrElse(default)
  }

  /**
   * Safely parse double with fallback
   */
  def parseDouble(value: String, default: Double = 0.0): Double = {
    value.replaceAll("[^0-9.]", "").toDoubleOption.getOrElse(default)
  }
}

/**
 * Pagination utilities
 */
object PaginationUtils {
  
  case class PaginationParams(page: Int = 1, pageSize: Int = 20) {
    val offset: Int = (page - 1) * pageSize
  }

  def paginate[T](items: List[T], page: Int = 1, pageSize: Int = 20): (List[T], Int) = {
    val params = PaginationParams(page, pageSize)
    val paginatedItems = items.slice(params.offset, params.offset + pageSize)
    (paginatedItems, (items.length + pageSize - 1) / pageSize) // Total pages
  }
}

/**
 * Retry utilities for handling transient failures
 */
object RetryUtils {
  
  def retryOperation[T](
    operation: () => Future[T],
    maxRetries: Int = 3,
    delayMs: Long = 100
  ): Future[T] = {
    def attempt(retryCount: Int): Future[T] = {
      operation().recoverWith {
        case exception if retryCount < maxRetries =>
          Thread.sleep(delayMs * (retryCount + 1))
          attempt(retryCount + 1)
        case exception =>
          Future.failed(exception)
      }
    }
    attempt(0)
  }
}
