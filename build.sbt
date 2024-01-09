import sbtrelease.ReleaseStateTransformations._

val scala_2_12: String = "2.12.18"
val scala_2_13: String = "2.13.12"

lazy val publishSettings = Seq(
  publishTo := sonatypePublishToBundle.value,
  scmInfo := Some(ScmInfo(
    url("https://github.com/guardian/mobile-logstash-encoder"),
    "scm:git:git@github.com/guardian/mobile-logstash-encoder"
  )),
  homepage := Some(url("https://github.com/guardian/mobile-logstash-encoder")),
  developers := List(Developer(
    id = "Guardian",
    name = "Guardian",
    email = null,
    url = url("https://github.com/guardian")
  )),
  crossScalaVersions := Seq(scala_2_12, scala_2_13),
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    releaseStepCommand("sonatypeBundleRelease"),
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)
val awsSdk2Version = "2.21.21"

lazy val root = (project in file("."))
  .settings(publishSettings)
  .settings(
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    organization := "com.gu",
    scalaVersion := scala_2_12,
    name := "mobile-logstash-encoder",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "autoscaling" % awsSdk2Version,
      "software.amazon.awssdk" % "ec2" % awsSdk2Version,
      "com.gu" %% "simple-configuration-core" % "1.5.8",
      "net.logstash.logback" % "logstash-logback-encoder" % "5.3",
      "ch.qos.logback" % "logback-core" % "1.4.14",
      "com.fasterxml.jackson.core" % "jackson-core" % "2.16.1",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.13.5",
      "io.netty" % "netty-codec" % "4.1.104.Final",
      "io.netty" % "netty-codec-http" % "4.1.104.Final",
      "io.netty" % "netty-codec-http2" % "4.1.104.Final",
      "io.netty" % "netty-common" % "4.1.104.Final",
      "org.specs2" %% "specs2-core" % "4.20.4" % "test"
    )
  )
