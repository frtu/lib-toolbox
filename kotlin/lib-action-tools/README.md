# Project - lib-action-tools

## About

Specialisation of Action framework dedicated to Tools & Functions.

### Auto expose Tool endpoints

Adding `spring-boot-starter-webflux` will enable Tool endpoints.

* `application.tools.endpoint.enabled` allow to enable `true` (default) or `false`
* `application.tools.endpoint.deployment-mode` configure endpoint `STATIC` (default) `DYNAMIC`

```yaml
application:
  tools:
    endpoint:
      enabled: true
      deployment-mode: STATIC
      url-prefix: "/v1/tools"
```

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

### 2.0.8

* Initial version
