# Statically compiled Groovy 6 container DSL

This example demonstrates a type-checking extension that redirects explicit
receiver calls on a DSL container to static methods in a separate class.
Those methods use extension-method-style signatures (the receiver is the
first parameter), but are not members of the container and are not supplied
through closure delegation.

## Run

```sh
gradle run
gradle test
gradle build
```

Expected application output:

```text
alpha | beta | gamma
alpha, beta
```

## Method selection

The DSL calls the same method name with different argument types:

```groovy
myContainer.display(' | ') // Commands.display(Container, String)
myContainer.display(2)     // Commands.display(Container, int)
```

The type-checking extension examines the explicit receiver type, argument
count, and inferred static types of the arguments. It searches the methods
declared on `Commands` and rewrites a uniquely matching call into an ordinary
static invocation, for example `Commands.display(myContainer, 2)`.

There is no name-to-method table. Adding an overload to `Commands` makes it
available to the DSL when its signature matches. Because the extension runs
before ordinary method resolution, it can also prevent a same-named Groovy
DGM extension from winning resolution.

The resolver is intentionally a teaching example, not a replacement for
Groovy's complete overload-resolution algorithm. It handles exact parameter
types, basic reference assignability, and primitive/wrapper pairing; it does
not yet model all Groovy coercions, generic substitutions, varargs, or
closure parameter inference.

## Tests and CI

`ContainerDslTest` verifies the result of the DSL calls and both overloads.
The GitHub Actions workflow in `.github/workflows/gradle.yml` runs
`gradle --no-daemon clean build` on pushes and pull requests. It installs
Temurin 17 and Gradle 8.14.

The project uses Groovy 6 and has no `src/main/resources` directory.
