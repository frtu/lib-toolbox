# Project - lib-action

## About

Generic framework to normalise actions & its metadata.

## Action metadata

* `ActionId` : Unique identifier for an action that human and LLM can recognise (can be a)
* `ActionMetadata` : Id, description & method signature

## Action execution

* `Action<INPUT, OUTPUT>` : marker interface to normalise what should come in & out
* `TypedAction` or `GenericAction` define if `execute()` function takes strongly typed or weakly typed.

## Action transition

* Check if `Action` return ot type `TypedAction` or `GenericAction` to execute next step
* OR return type `ActionMetadata` to fetch `id` and do the rest

### Advices

My recommendation is to keep the strongly type as much as possible in the "system boundaries" (when it comes IN or OUT
of our system) because they require concrete `Class` & JAR imports.

For your internal system, you may choose to use weakly typed to skip the need for recompilation, JAR updates & heavy
change management process (even if some quick testing are necessary).

## Import

Import using Maven :

```XML

<dependency>
    <groupId>com.github.frtu.libs</groupId>
    <artifactId>lib-action</artifactId>
    <version>${frtu-libs.version}</version>
</dependency>
```

or gradle

```
implementation("com.github.frtu.libs:lib-action:${Versions.frtu_libs}")
```

or TOML

```
lib-action = { group = "com.github.frtu.libs", name = "lib-action", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-action.svg?label=latest%20release%20:%20lib-action"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.12

* Enrich all `ActionMetadata` & `ActionTransitionImpl` with `category` & `subCategory`

### 2.0.8

* Initial version
