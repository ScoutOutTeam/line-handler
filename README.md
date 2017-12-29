# line-handler
Spring Boot project integrated with LINE Messaging API and LINE Log-in API. Ready to handle incoming messages and SSO with LINE Login in LINE Channel

Only need to set two parameters below. Note: handler.path is not required.

```
line.bot:
  channel-token: "Your LINE Bot channel token"
  channel-secret: "Your LINE Bot channel secret"
  handler.path: /callback
  
line.login:
  channel-token: "Your LINE Log-in channel ID"
  channel-secret: "Your LINE Log-in channel secret"
  redirect-uri: 
```

### References
https://developers.line.me/en/docs/messaging-api/overview/  
https://developers.line.me/en/docs/line-login/web/integrate-line-login/
