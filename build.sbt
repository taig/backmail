import sbtcrossproject.CrossProject
val Version = new {
  val Circe = "0.14.10"
  val MUnit = "1.0.2"
  val Scala = "3.3.3"
}

def module(identifier: Option[String]): CrossProject = {
  CrossProject(identifier.getOrElse("root"), file(identifier.fold(".")("modules/" + _)))(JVMPlatform, JSPlatform)
    .crossType(CrossType.Pure)
    .withoutSuffixFor(JVMPlatform)
    .build()
    .settings(
      Compile / scalacOptions ++= "-source:future" :: "-rewrite" :: "-new-syntax" :: "-Wunused:all" :: Nil,
      name := "backmail" + identifier.fold("")("-" + _)
    )
}

inThisBuild(
  Def.settings(
    developers := List(Developer("taig", "Niklas Klein", "mail@taig.io", url("https://taig.io/"))),
    dynverVTagPrefix := false,
    homepage := Some(url("https://github.com/taig/backmail/")),
    licenses := List("MIT" -> url("https://raw.githubusercontent.com/taig/backmail/main/LICENSE")),
    organization := "io.taig",
    organizationHomepage := Some(url("https://taig.io/")),
    scalaVersion := Version.Scala,
    versionScheme := Some("early-semver")
  )
)

lazy val root = module(identifier = None)
  .enablePlugins(BlowoutYamlPlugin)
  .settings(noPublishSettings)
  .settings(
    blowoutGenerators ++= {
      val workflows = file(".github") / "workflows"
      BlowoutYamlGenerator.lzy(workflows / "main.yml", GitHubActionsGenerator.main) ::
        BlowoutYamlGenerator.lzy(workflows / "branches.yml", GitHubActionsGenerator.branches) ::
        Nil
    }
  )
  .aggregate(core, circe)

lazy val core = module(identifier = Some("core"))
  .settings(
    libraryDependencies ++=
      "org.scalameta" %%% "munit" % Version.MUnit % "test" ::
        Nil
  )

lazy val circe = module(identifier = Some("circe"))
  .settings(
    libraryDependencies ++=
      "io.circe" %%% "circe-core" % Version.Circe ::
        Nil
  )
  .dependsOn(core % "compile->compile;test->test")
