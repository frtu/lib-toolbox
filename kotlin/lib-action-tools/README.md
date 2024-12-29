# Project - lib-action-tools

## About

Specialisation of Action framework dedicated to Tools & Functions.

## Import

Import using Maven :

```XML

<dependency>
    <groupId>com.github.frtu.libs</groupId>
    <artifactId>lib-action-tools</artifactId>
    <version>${frtu-libs.version}</version>
</dependency>
```

or gradle

```
implementation("com.github.frtu.libs:lib-action-tools:${Versions.frtu_libs}")
```

or TOML

```
lib-action-tools = { group = "com.github.frtu.libs", name = "lib-action-tools", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-action-tools.svg?label=latest%20release%20:%20lib-action-tools"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.12

* Enrich all `Tool`s with `category` & `subCategory`

### 2.0.9

* Add `StructuredTool` to extend `Tool` & `TypedAction<INPUT, OUTPUT>` for strongly typed execution
* Allow `StructuredToolExecuter` to extend `StructuredTool<INPUT, OUTPUT>` & provide internal generic to type mapping
* Provide `StructuredToolExecuter.create()` to create lamba encapsulation as `StructuredTool`

### 2.0.8

* Initial version
