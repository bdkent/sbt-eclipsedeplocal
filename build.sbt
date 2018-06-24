import sbt.Keys._

lazy val pomSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  homepage := Some(url("https://github.com/bdkent/sbt-eclipsedeplocal")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/bdkent/sbt-eclipsedeplocal"),
      "git@github.com:bdkent/sbt-eclipsedeplocal.git"
    )
  ),
  developers := List(
    Developer(id="bdkent", name="Brian Kent", email="bkent314@gmail.com", url=url("https://github.com/bdkent"))
  )
)

releasePublishArtifactsAction := PgpKeys.publishSigned.value

lazy val root = (project in file(".")).
  settings(
    name := "sbt-eclipsedeplocal",
    organization := "com.github.bdkent",
    scalaVersion := "2.12.6",
    sbtPlugin := true,
    sbtVersion := "1.1.6"
  ).
  settings(pomSettings)
 