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

## Release notes

### 1.2.5 - Current version

* Initial version
