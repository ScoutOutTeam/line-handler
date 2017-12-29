# line-handler
Spring Boot project integrated with LINE Messaging API and LINE Log-in API. Ready to handle incoming messages and SSO with LINE Login in LINE Channel

Please set required parameters below.

```
line.bot:
  channel-token: "Your LINE Bot channel token"
  channel-secret: "Your LINE Bot channel secret"
  handler.path: /callback
  
line.login:
  channel-token: "Your LINE Log-in channel ID"
  channel-secret: "Your LINE Log-in channel secret"
  redirect-uri: "(example)https://line-handler-test.scoutout.net/auth"
```
Build with Gradle wrapper `./gradlew clean build` and run with `./gradlew bootrun`

### References
https://developers.line.me/en/docs/messaging-api/overview/  
https://developers.line.me/en/docs/line-login/web/integrate-line-login/
