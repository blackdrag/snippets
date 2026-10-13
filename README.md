# Statically compiled Groovy container DSL

This example demonstrates a type-checking extension that redirects explicit
receiver calls on a DSL container to same-named static methods in a separate
class. The methods use extension-method-style signatures (the receiver is
the first parameter), but are not members of the container and are not
provided through closure delegation.

## Run

```sh
gradle run
```

Expected output:

```text
DSL item: alpha
DSL item: beta
DSL item: gamma
```

## Files

- `Container.groovy`: the data container; it has no `each` method.
- `Commands.groovy`: DSL operations as ordinary static methods whose first
  parameter is the receiver.
- `ContainerDslExtension.groovy`: the type-checking extension. It inspects
  calls on `Container`, looks up a static method with the same name in
  `Commands`, and rewrites the AST call to `Commands.method(container, ...)`.
- `Main.groovy`: the statically compiled DSL usage.

## Why the type-checking extension matters

Groovy already has an `each` extension method for iterables. Without
intervention, `myContainer.each { ... }` can resolve to Groovy's built-in
method instead of the DSL implementation. The extension runs before normal
method resolution and rewrites matching calls to explicit static calls.
The command name is not mapped manually: adding another static method to
`Commands` with `Container` as its first parameter makes that method
available under the same name.

The example uses an explicitly typed closure parameter to keep the focus on
method resolution. It does not use `@DelegatesTo`, closure delegation, or
methods declared on `Container`.

The Gradle build has not been executed in this environment, so compilation
and runtime behavior still need verification.
