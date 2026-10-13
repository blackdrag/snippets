# Statically compiled Groovy command DSL

A tiny DSL showing command methods implemented as Groovy extension methods
and checked with a type-checking extension.

## Run

```sh
gradle run
```

Expected output:

```text
Hello from the DSL
Pausing for 2 second(s)
Finished
```

## How it works

- `Commands` uses extension-method style: each public static method takes
  `CommandContext` as its first argument.
- `META-INF/services/org.codehaus.groovy.runtime.ExtensionModule` registers
  `Commands` as a Groovy extension module. Groovy resolves command names from
  the methods themselves; there is no separate command-to-method map.
- `CommandContext.run` declares the closure delegate with `@DelegatesTo`,
  so the DSL body is type-checked against `CommandContext`.
- `commandDsl.groovy` is a type-checking extension. It observes method
  selection and identifies extension methods supplied by `demo.Commands`.
  The selected extension method's real signature is available to the static
  compiler, so these calls do not need `makeDynamic`.

Try changing `pause 2` to `pause 'soon'` or calling `unknownCommand()`.
Compilation should fail rather than silently accepting an unknown command.

The type-checking extension deliberately does not contain a list of command
names. Adding a method to `Commands` and recompiling is enough to make it
available as an extension method.
