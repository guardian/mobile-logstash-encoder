import sbt.Keys.scalaVersion

lazy val root = (project in file("."))
  .settings(
    organization := "com.gu",

    scalaVersion := "2.12.7",

    name := "mobile-logstash-encoder",

    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "com.gu" %% "simple-configuration-core" % "1.5.0",
      "net.logstash.logback" % "logstash-logback-encoder" % "5.2",
      "ch.qos.logback" % "logback-core" % "1.2.3",
      "com.typesafe.play" %% "play-json" % "2.6.10"
    )
  )