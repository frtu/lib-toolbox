# Project - lib-test-utils

## About

Lib for generating mock object. Also include [java-faker](https://github.com/DiUS/java-faker) to facilitate strong-typed
field.

```XML
<properties>
  <frtu.lib.version>x.y.z</frtu.lib.version>
</properties>

<dependency>
    <groupId>com.github.frtu.libs</groupId>
    <artifactId>lib-test-utils</artifactId>
    <version>${frtu.lib.version}</version>
</dependency>
```

## Resilience

Generate random exception using :

```kotlin
ChaosGenerator().raiseException("Error Message returned")
```

OR

```java
new ChaosGenerator().raiseException("Error Message returned")
```

## History

* Migrated
  from [governance-toolbox](https://github.com/frtu/governance-toolbox/tree/master/libraries/library-generators).
* Existed under :

```XML
<dependency>
    <groupId>com.github.frtu.governance</groupId>
    <artifactId>library-generators</artifactId>
    <version>${governance-libraries.version}</version>
</dependency>
```

## Release notes

### 1.1.3

* Migrated version with latest base-pom.xml