# Project - sample-spring-boot-slack

## About

Sample application that allow to start Slack connectivity along with spring boot lifecycle.

## Configuration

Don't forget to configure your application token using :

```yaml
application:
  name: sample-spring-boot-slack
  slack.app:
    token: ${SLACK_APP_TOKEN:xapp-xxx}
    signing-secret: ${SLACK_APP_SIGNING_SECRET:yyy}
    bot-oauth-token: ${SLACK_APP_BOT_OAUTH_TOKEN:xoxb-zzz}
```