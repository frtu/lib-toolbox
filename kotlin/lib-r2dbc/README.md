# Project - lib-r2dbc

## About

Persistence for coroutine using suspend functions. R2DBC will be used to achieve this goal.

## Starting up

Extend Spring Data interface : ```CoroutineCrudRepository```

For
ex [IEmailRepository](https://github.com/frtu/sample-code/blob/master/event-notification-mails/event-notification-web-coroutine-flow/src/main/kotlin/com/github/frtu/coroutine/persistence/IEmailRepository.kt):

```
@Repository
interface IEmailRepository : CoroutineCrudRepository<Email, UUID> {
...
}
```

Also import class from https://github.com/frtu/sample-code/tree/master/persistence-r2dbc/src/main/kotlin/com/github/frtu/persistence/r2dbc/query