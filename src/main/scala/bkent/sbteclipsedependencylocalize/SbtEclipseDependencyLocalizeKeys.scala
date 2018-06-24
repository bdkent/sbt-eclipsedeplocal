package bkent.sbteclipsedependencylocalize

import sbt._

trait SbtEclipseDependencyLocalizeKeys {

  val edlLocalizeDependency = inputKey[Unit]("localizes the given ModuleID")

  val edlExcludedOrganizations = settingKey[Seq[String]]("ModuleID organizations to exclude from localization")

  val edlIncludedOrganizations = settingKey[Seq[String]]("ModuleID organizations to include from localization")

  val edlExcludedConfigurations = settingKey[Seq[Option[String]]]("ModuleID configuratons to exclude from localization")

  val edlIncludedConfigurations = settingKey[Seq[Option[String]]]("ModuleID configuratons to include from localization")

}
