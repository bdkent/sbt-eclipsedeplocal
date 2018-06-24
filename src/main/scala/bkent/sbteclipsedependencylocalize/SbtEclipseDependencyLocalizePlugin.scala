package bkent.sbteclipsedependencylocalize

import sbt._
import Keys._
import scala.collection.Seq
import java.nio.file.Path
import complete.DefaultParsers._
import sbt.complete.Parser

object SbtEclipseDependencyLocalizePlugin extends AutoPlugin {

  object autoImport extends SbtEclipseDependencyLocalizeKeys {

  }

  import autoImport._

  def parser: Def.Initialize[State => Parser[LocalizeArg]] = {
    Def.setting { (state: State) =>
      ArgParser(filterLibraryDependencies(state, edlExcludedOrganizations.value, edlIncludedOrganizations.value, edlExcludedConfigurations.value, edlIncludedConfigurations.value))
    }
  }

  def filterLibraryDependencies(state: State, excludedOrganizations: Seq[String], includedOrganizations: Seq[String], excludedConfigurations: Seq[Option[String]], includedConfigurations: Seq[Option[String]]): Seq[ModuleID] = {
    val extracted = Project.extract(state)
    import extracted._

    val allLibraryDependencies = structure.allProjectRefs.flatMap({ p =>
      get(libraryDependencies in p)
    }).distinct

    val filters: Seq[ModuleID => Boolean] = List(
      (m: ModuleID) => {
        excludedOrganizations.toList match {
          case Nil => true
          case xs  => !xs.contains(m.organization)
        }
      },
      (m: ModuleID) => {
        includedOrganizations.toList match {
          case Nil => true
          case xs  => xs.contains(m.organization)
        }
      },
      (m: ModuleID) => {
        excludedConfigurations.toList match {
          case Nil => true
          case xs  => !xs.contains(m.configurations)
        }
      },
      (m: ModuleID) => {
        includedConfigurations.toList match {
          case Nil => true
          case xs  => xs.contains(m.configurations)
        }
      }
    )

    allLibraryDependencies.filter({ m =>
      filters.foldLeft(true)({
        case (acc, f) =>
          acc && f(m)
      })
    })
  }

  lazy val baseGhDocVerSettings = Seq(

    edlExcludedOrganizations := Seq("org.scala-js", "org.scala-lang"),

    edlIncludedOrganizations := Seq(),

    edlExcludedConfigurations := Seq(),

    edlIncludedConfigurations := Seq(None),

    edlLocalizeDependency := {

      val deps = filterLibraryDependencies(state.value, edlExcludedOrganizations.value, edlIncludedOrganizations.value, edlExcludedConfigurations.value, edlIncludedConfigurations.value)

      if (deps.isEmpty) {
        println("no library dependencies to localize...")
      } else {
        val args = parser.parsed

        val dir: Path = baseDirectory.value.toPath

        EclipseDependencyLocalize.localize(dir, args)

        println("within Scala IDE:")
        println("\t1) refresh affected projects")
        println("\t2) run `Project -> Clean... -> Clean all projects`")
      }
    }
  )

  override lazy val projectSettings = baseGhDocVerSettings
}

object ArgParser {

  def apply(libraryDependencies: Seq[ModuleID]): Parser[LocalizeArg] = {

    val knownModuleParser: Parser[ModuleID] = {
      if (libraryDependencies.isEmpty) {
        failure("no library dependencies...")
      } else {
        libraryDependencies.map(m => token(literal(m.toString)).map(_ => m)).reduce(_ | _)
      }
    }

    val moduleParser: Parser[ModuleID] = knownModuleParser

    val argParser: Parser[LocalizeArg] = (moduleParser ~ (Space.+ ~> StringBasic).?).map({ case (m, loc) => LocalizeArg(m, loc.getOrElse(m.name)) })

    Space.+ ~> argParser
  }

}
