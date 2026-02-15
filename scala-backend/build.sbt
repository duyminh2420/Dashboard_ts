name := "dashboard-backend"
version := "1.0.0"
scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "dashboard-backend",
    version := "1.0.0"
  )

// HTTP and JSON handling
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.5.3",
  "com.typesafe.akka" %% "akka-stream" % "2.8.5",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
  "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",
  "io.circe" %% "circe-core" % "0.14.6",
  "io.circe" %% "circe-generic" % "0.14.6",
  "io.circe" %% "circe-parser" % "0.14.6",
  
  // Database
  "org.scalikejdbc" %% "scalikejdbc" % "4.1.1",
  "org.scalikejdbc" %% "scalikejdbc-config" % "4.1.1",
  "org.postgresql" % "postgresql" % "42.7.1",
  "com.h2database" % "h2" % "2.2.224",
  
  // CORS handling
  "ch.megard" %% "akka-http-cors" % "1.2.0",
  
  // Logging
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.4.12",
  
  // Config
  "com.typesafe" % "config" % "1.4.3",
  
  // Testing
  "org.scalatest" %% "scalatest" % "3.2.17" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.5.3" % Test
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Xlint",
  "-Ywarn-unused"
)

mainClass in Compile := Some("com.dashboard.Main")
