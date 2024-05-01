import ReleaseTransformations.*
import sbtversionpolicy.withsbtrelease.ReleaseVersion
val scala_2_12: String = "2.12.19"
val scala_2_13: String = "2.13.13"

ThisBuild / scalacOptions := Seq("-deprecation", "-release:11")
ThisBuild / scalaVersion := scala_2_12
ThisBuild / crossScalaVersions := Seq(
  scalaVersion.value,
  scala_2_13,
)

val awsSdk2Version = "2.25.13"

lazy val root = (project in file("."))
  .settings(
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    organization := "com.gu",
    name := "mobile-logstash-encoder",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "autoscaling" % awsSdk2Version,
      "software.amazon.awssdk" % "ec2" % awsSdk2Version,
      "com.gu" %% "simple-configuration-core" % "1.7.0",
      "net.logstash.logback" % "logstash-logback-encoder" % "7.4",
      "ch.qos.logback" % "logback-core" % "1.5.3",
      "com.fasterxml.jackson.core" % "jackson-core" % "2.16.2",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.17.0",
      "io.netty" % "netty-codec" % "4.1.107.Final",
      "io.netty" % "netty-codec-http" % "4.1.107.Final",
      "io.netty" % "netty-codec-http2" % "4.1.107.Final",
      "io.netty" % "netty-common" % "4.1.107.Final",
      "org.specs2" %% "specs2-core" % "4.20.5" % "test"
    ),
    publish / skip := true,
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
