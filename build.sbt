import ReleaseTransformations.*
import sbtversionpolicy.withsbtrelease.ReleaseVersion
val scala_2_12: String = "2.12.20"
val scala_2_13: String = "2.13.16"

ThisBuild / scalacOptions := Seq("-deprecation", "-release:11")
ThisBuild / scalaVersion := scala_2_13
ThisBuild / crossScalaVersions := Seq(
  scalaVersion.value,
  scala_2_12,
)
val awsSdk2Version = "2.30.16"

lazy val root = (project in file("."))
  .settings(
    licenses := Seq(License.Apache2),
    organization := "com.gu",
    name := "mobile-logstash-encoder",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "autoscaling" % awsSdk2Version,
      "software.amazon.awssdk" % "ec2" % awsSdk2Version,
      "com.gu" %% "simple-configuration-core" % "5.0.0",
      "net.logstash.logback" % "logstash-logback-encoder" % "8.0",
      "ch.qos.logback" % "logback-core" % "1.5.17",
      "com.fasterxml.jackson.core" % "jackson-core" % "2.18.2",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.18.2",
      "io.netty" % "netty-codec" % "4.1.118.Final",
      "io.netty" % "netty-codec-http" % "4.1.118.Final",
      "io.netty" % "netty-codec-http2" % "4.1.118.Final",
      "io.netty" % "netty-common" % "4.1.118.Final",
      "org.specs2" %% "specs2-core" % "4.20.9" % "test"
    ),
    releaseVersion := ReleaseVersion.fromAggregatedAssessedCompatibilityWithLatestRelease().value,
    releaseCrossBuild := true, // true if you cross-build the project for multiple Scala versions
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      setNextVersion,
      commitNextVersion
    ),
    Test / testOptions +=
      Tests.Argument(TestFrameworks.ScalaTest, "-u", s"test-results/scala-${scalaVersion.value}", "-o")
  )
