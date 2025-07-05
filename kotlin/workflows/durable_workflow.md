# Durable Workflow

## Context

### What is durable Execution using Temporal

[Temporal](https://temporal.io/) provides an **Orchestration engine** for [Workflow Execution](https://docs.temporal.io/workflow-execution). Any particular execution represent a business flow & is characterised by its triggering input parameters (ex: create an `IT support` case for user `abc@test.com`).

While usual platform run execution below a few second, Temporal provide **Durability** which allows execution to span over hours or even months. The workflow context is persisted and allow any workflow instance to be hydrated on usage.

### Workflow defintion (heavy vs lightweight)

To allow to run execution in a repeatitive & deterministic manner, Workflow definition can be capture in different format :

* `Workflow as Code` : a blueprint is capture into **Code**, which follow regular code SDLC (compilation, test, deploy to prod & rollback). `One` single code version is run at every specific moment that we can **deploy or rollback**


* `Workflow Graph` : a blueprint is capture in a form of a Graph (DAG) which allow to quickly generate, deploy to prod. A versionning mechanism should allow to run multiple execution in parallel each using a **specific versions** of the workflow 
