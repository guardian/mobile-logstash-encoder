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
val awsSdk2Version = "2.31.61"

lazy val root = (project in file("."))
  .settings(
    licenses := Seq(License.Apache2),
    organization := "com.gu",
    name := "mobile-logstash-encoder",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "autoscaling" % awsSdk2Version,
      "software.amazon.awssdk" % "ec2" % awsSdk2Version,
      "net.logstash.logback" % "logstash-logback-encoder" % "8.1",
      "com.gu" %% "simple-configuration-core" % "5.1.2",
      "ch.qos.logback" % "logback-core" % "1.5.18",
      "com.fasterxml.jackson.core" % "jackson-core" % "2.19.1",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.19.1",
      "io.netty" % "netty-codec" % "4.2.2.Final",
      "io.netty" % "netty-codec-http" % "4.2.2.Final",
      "io.netty" % "netty-codec-http2" % "4.2.2.Final",
      "io.netty" % "netty-common" % "4.2.2.Final",
      "org.specs2" %% "specs2-core" % "4.21.0" % "test"
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
