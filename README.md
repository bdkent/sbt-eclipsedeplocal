# sbt-eclipsedeplocal
[sbt](scala-sbt.org) plugin to rewrite [Scala IDE](scala-ide.org) / Eclipse classpath of a dependency as a local project

## Overview

`sbt-eclipsedeplocal` adds one main task:

<dl>
  <dt>edlLocalizeDependency</dt>
  <dd>rewrites the <code>.classpath</code> of a given library dependency as a local project</dd>
</dl>


## Installation

NOTE: requires sbt 1.x

add to `project/plugins.sbt`:

```scala
addSbtPlugin("com.github.bdkent" % "sbt-eclipsedeplocal" % "0.1.0")
```

enable in `build.sbt`:
```scala
enablePlugins(SbtEclipseDependencyLocalizePlugin)
```

## Usage

### edlLocalizeDependency

The usage of this task is easiest to explain by way of an example:

Let's assume you have two projects, a library (`awesome-library`) and an app (`awesome-app`), where the app includes the library.

So `awesome-app`'s `build.sbt` includes it like so:
```scala
libraryDependencies += "org.foo" % "awesome-library" % "0.1.0"
```

You are actively developing both in Scala IDE, so you have both projects open as `/awesome-app` and `/awesome-library`.

Using `sbt-eclipse`'s `eclipse` command, the `/awesome-app` project will depend on the published artifact (assumedly somewhere in `$HOME/.ivy2`), not the (unrelated from sbt's perspective) Scala IDE project.

This is where the `edlLocalizeDependency` task comes in!

The following will rewrite the `.classpath` in `/awesome-app` to reference the `/awesome-library` Scala IDE project instead of the publsihed artifact:
```
sbt:awesome-app> edlLocalizeDependency  org.foo:awesome-library:0.1.0  awesome-library
```

If the Scala IDE's project name is the same as the name of the artifact (which is the case in the example), you can drop the redundant second parameter:
```
sbt:awesome-app> edlLocalizeDependency  org.foo:awesome-library:0.1.0
```



