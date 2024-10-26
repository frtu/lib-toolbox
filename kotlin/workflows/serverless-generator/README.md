# Project - serverless-generator

## About

Generator for serverless DSL.

Ex :

```kotlin
workflow {
    name = "Workflow_${UUID.randomUUID()}"
    states {
        +sleep(sleepStateName) {
            duration = "PT5S"
            transition = operationStateName
        }
        +operation(operationStateName) {
            +(call(ServiceCall::query) using {
                ServiceRequest::id with "dfed7335-d865-4190-8e59-8604ebdb7963"
            })
            +(call(ServiceCall::query) using {
                ServiceRequest::id with "\${ variable.id }"
                ServiceRequest::name with "\${ variable.name }"
            })
            transition = switchStateName
        }
        +switch(switchStateName) {
            +case("\${ #event.type = 'validation.init' }", name = "validation.init") {
                transition = "ValidationInitialized"
            }
            +case("\${ #event.type = 'validation.approved' }", name = "validation.approved") {
                transition = "ValidationApproved"
            }
            default(transition = "DefaultState")
        }
    }
}
```

## Import

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>serverless-generator</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

or

```
implementation("com.github.frtu.libs:serverless-generator:${Versions.frtu_libs}")
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/serverless-generator.svg?label=latest%20release%20:%20serverless-generator"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22serverless-generator%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.7

* Reactivate `serverless-generator`

### 1.2.5

* Initial version
