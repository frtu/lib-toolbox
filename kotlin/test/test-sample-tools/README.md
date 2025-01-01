# Project - test-sample-tools

## About

Sample Tools & Functions for testing around Tools :

### Usage

Can import `SampleToolConfig` into your Spring Boot test to initialise Tools creation.
Should return `SampleToolConfig.NUMBER_OF_TOOLS` (ex : 4 `Tool`s in total)

Tools

* `IdentityTool`
* `CurrentWeatherTool`

Functions

* `CurrentWeatherFunction`
* `WeatherForecastFunction`

Each `Tool` spring bean **name** are captured in `xxTool.TOOL_NAME`

## Import

Import using Maven :

```XML
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>test-sample-tools</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

or gradle

```
testImplementation("com.github.frtu.libs:test-sample-tools:${Versions.frtu_libs}")
```

or TOML

```
test-sample-tools = { group = "com.github.frtu.libs", name = "test-sample-tools", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/test-sample-tools.svg?label=latest%20release%20:%20test-sample-tools"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.9

* Initial version
