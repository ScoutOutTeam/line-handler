# line-handler
Spring Boot project integrated with LINE Messaging API. Ready to handle incoming messages in LINE Channel

Only need to set two parameters below. Note: handler.path is not required.

```
line.bot:
  channel-token: "Your channel token"
  channel-secret: "Your channel secret"
  handler.path: /callback
```

### References
https://developers.line.me/en/docs/messaging-api/overview/  
https://github.com/line/line-bot-sdk-java/tree/master/line-bot-spring-boot
