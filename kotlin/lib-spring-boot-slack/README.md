# Project - lib-spring-boot-slack

## About

Allow to connect to Slack & receive message when starting spring boot.

## Enablement

This project intend to use [socket mode](https://slack.dev/java-slack-sdk/guides/socket-mode) which rely on Websocket &
avoid the need to open HTTP webhook callback.

When creating and installing your app, don't forget to

* Go to Settings > Basic Information > Add a new App-Level Token with the `connections:write` scope
* Get the generated token value that starts with xapp-

* Go to Settings > Socket Mode > Turn on `Enable Socket Mode`
* Configure the features (without setting Request URLs)

* Install the app to receive bot/user tokens (bot: xoxb-, user: xoxp-)

### Alternative

You can also use [Slack Basic API](https://slack.dev/java-slack-sdk/guides/web-api-basics/) that offers a simple way to
send message.

## Features

[Events subscription](https://api.slack.com/apis/events-api) :

* Listen to event when bot is being called : `AppMentionEvent`

Settings > Features > [Slash commands](https://slack.dev/java-slack-sdk/guides/slash-commands)

* Respond to commands : `/hello`

## Usage

Import Spring Config `com.github.frtu.kotlin.spring.slack.config.SlackConfig` into your application.

### Slack Event

Register Slack events using :

```kotlin
@Configuration
class EventHandlerFactory {
    @Bean
    fun messageEventHandler(): Pair<Class<MessageEvent>, BoltEventHandler<MessageEvent>> =
        MessageEventHandler().toPair()

    @Bean
    fun appMentionEventHandler(): Pair<Class<AppMentionEvent>, BoltEventHandler<AppMentionEvent>> = Pair(
        AppMentionEvent::class.java,
        BoltEventHandler { req: EventsApiPayload<AppMentionEvent>, ctx: EventContext ->
            ctx.say("Hi there!")
            ctx.ack()
        })
}
```

### Slack commands

Register Slack commands using :

```kotlin
@Configuration
class CommandFactory {
    @Bean
    fun hello(): SlashCommandHandler = SlashCommandHandler { req, ctx ->
        ctx.logger.debug("Command /hello called")

        val response = ctx.client().chatPostMessage { r ->
            r.channel(ctx.channelId).text(":wave: How are you?")
        }
        println(response.message)
        ctx.ack(":wave: Hello!")
    }
}
```

## Import

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>lib-spring-boot-slack</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

or

```
implementation("com.github.frtu.libs:lib-spring-boot-slack:${Versions.frtu_libs}")
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-spring-boot-slack.svg?label=latest%20release%20:%20lib-spring-boot-slack"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.2 - Current version

### 2.0.1

* Initial version
