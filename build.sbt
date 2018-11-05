import sbtrelease.ReleaseStateTransformations._

lazy val publishSettings = Seq(
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  bintrayOrganization := Some("guardian"),
  bintrayRepository := "platforms",
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    releaseStepTask(bintrayRelease),
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)

lazy val root = (project in file("."))
  .settings(publishSettings)
  .settings(
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    organization := "com.gu",
    scalaVersion := "2.12.7",
    name := "mobile-logstash-encoder",
    libraryDependencies ++= Seq(
      "com.gu" %% "simple-configuration-core" % "1.5.0",
      "net.logstash.logback" % "logstash-logback-encoder" % "5.2",
      "ch.qos.logback" % "logback-core" % "1.2.3",
      "com.typesafe.play" %% "play-json" % "2.6.10"
    )
  )