package bkent.sbteclipsedependencylocalize

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

import scala.xml.Node
import scala.xml.XML
import sbt.librarymanagement.ModuleID

case class LocalizeArg(moduleId: ModuleID, localProjectName: String)

object LocalizeArg {
  implicit class LocalizeArgEnhancements(self: LocalizeArg) {
    def dependencyName = (self.moduleId.organization :: self.moduleId.name :: self.moduleId.revision :: Nil).mkString("/")
  }
}

object EclipseDependencyLocalize {

  def localize(dir: Path, arg: LocalizeArg): Unit = {

    val dependencyName = arg.dependencyName
    val localProjectName = arg.localProjectName

    def isDependencyOfInterest(classpathEntryNode: Node): Boolean = {
      classpathEntryNode.label == "classpathentry" && (classpathEntryNode \ "@kind").toString == "lib" && (classpathEntryNode \ "@path").toString.contains(s"/${dependencyName}")
    }

    Files.walkFileTree(dir, new SimpleFileVisitor[Path]() {
      override def visitFile(file: Path, attrs: BasicFileAttributes) = {
        if (file.getFileName.toString == ".classpath") {
          val xml = XML.loadFile(file.toFile)
          val children = xml.child.toSeq
          val localizeChildren = xml.child.toSeq.map({ elem =>
            elem match {
              case cpe if isDependencyOfInterest(cpe) => {
                val path = s"/${localProjectName}"
                <classpathentry combineaccessrules="false" kind="src" path={ path }/>
              }
              case _ => {
                elem
              }
            }
          })

          if (children != localizeChildren) {
            val localizedXml = xml.copy(child = localizeChildren)
            XML.save(file.toString, localizedXml)
          }
        }

        FileVisitResult.CONTINUE
      }

    })
  }
}
