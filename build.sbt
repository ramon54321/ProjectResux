
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

scalacOptions += "-Ypartial-unification"

lazy val root = project.in(file("."))
  .aggregate(server, client)

lazy val main = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(
    version := "0.0.1",
    libraryDependencies += "com.github.benhutchison" %%% "prickle" % "1.1.13",
    libraryDependencies += "org.typelevel" %%% "cats-macros" % "2.0.0",
    libraryDependencies += "org.typelevel" %%% "cats-kernel" % "2.0.0",
    libraryDependencies += "org.typelevel" %%% "cats-core" % "2.0.0",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % "0.12.3",
      "io.circe" %%% "circe-generic" % "0.12.3",
      "io.circe" %%% "circe-parser" % "0.12.3"
    )
  )
  .jsSettings(
    name := "client",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.7",
  )
  .jvmSettings(
    name := "server",
    mainClass in (Compile, run) := Some("sux.server.Main"),
    mainClass in assembly := Some("sux.server.Main"),
    assemblyJarName in assembly := "sux_server.jar",
    libraryDependencies += "org.java-websocket" % "Java-WebSocket" % "1.4.0",
    libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.2"
  )

lazy val scratch = project.in(file("scratch")).dependsOn(main.jvm)

lazy val server = main.jvm
lazy val client = main.js
