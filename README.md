# sbt-eclipsedeplocal
sbt plugin to rewrite eclipse classpath of a dependency as a local project

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
