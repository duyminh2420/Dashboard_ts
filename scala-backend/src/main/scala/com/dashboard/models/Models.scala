package com.dashboard.models

import java.time.LocalDateTime
import io.circe.generic.semiauto._
import io.circe.{Encoder, Decoder}

// User Model
case class User(
  id: Int,
  firstName: String,
  lastName: String,
  email: String,
  phone: String,
  img: String,
  createdAt: String,
  verified: Boolean = false
)

object User {
  implicit val encoder: Encoder[User] = deriveEncoder[User]
  implicit val decoder: Decoder[User] = deriveDecoder[User]
}

// Product Model
case class Product(
  id: Int,
  title: String,
  color: String,
  producer: String,
  price: String,
  img: String,
  createdAt: String,
  inStock: Boolean = true
)

object Product {
  implicit val encoder: Encoder[Product] = deriveEncoder[Product]
  implicit val decoder: Decoder[Product] = deriveDecoder[Product]
}

// Activity Model
case class Activity(
  id: Int,
  userId: Int,
  text: String,
  time: String
)

object Activity {
  implicit val encoder: Encoder[Activity] = deriveEncoder[Activity]
  implicit val decoder: Decoder[Activity] = deriveDecoder[Activity]
}

// Chart Data Models
case class ChartDataPoint(
  name: String,
  value: Double
)

object ChartDataPoint {
  implicit val encoder: Encoder[ChartDataPoint] = deriveEncoder[ChartDataPoint]
  implicit val decoder: Decoder[ChartDataPoint] = deriveDecoder[ChartDataPoint]
}

case class ChartBox(
  color: String,
  icon: String,
  title: String,
  number: String,
  dataKey: String,
  percentage: Int,
  chartData: List[ChartDataPoint]
)

object ChartBox {
  implicit val encoder: Encoder[ChartBox] = deriveEncoder[ChartBox]
  implicit val decoder: Decoder[ChartBox] = deriveDecoder[ChartBox]
}

// Pagination
case class PaginatedResponse[T](
  data: List[T],
  total: Int,
  page: Int,
  pageSize: Int
)

object PaginatedResponse {
  implicit def encoder[T](implicit enc: Encoder[T]): Encoder[PaginatedResponse[T]] = deriveEncoder[PaginatedResponse[T]]
  implicit def decoder[T](implicit dec: Decoder[T]): Decoder[PaginatedResponse[T]] = deriveDecoder[PaginatedResponse[T]]
}

// API Response wrapper
case class ApiResponse[T](
  success: Boolean,
  data: Option[T],
  message: Option[String],
  timestamp: Long
)

object ApiResponse {
  implicit def encoder[T](implicit enc: Encoder[T]): Encoder[ApiResponse[T]] = deriveEncoder[ApiResponse[T]]
  implicit def decoder[T](implicit dec: Decoder[T]): Decoder[ApiResponse[T]] = deriveDecoder[ApiResponse[T]]

  def success[T](data: T): ApiResponse[T] = ApiResponse(
    success = true,
    data = Some(data),
    message = None,
    timestamp = System.currentTimeMillis()
  )

  def error[T](message: String): ApiResponse[T] = ApiResponse(
    success = false,
    data = None,
    message = Some(message),
    timestamp = System.currentTimeMillis()
  )
}
