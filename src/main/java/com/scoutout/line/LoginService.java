package com.scoutout.line;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.scoutout.line.bean.LineLoginBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class LoginService {
    private static final Logger logger = LoggerFactory
            .getLogger(LoginService.class);

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @GetMapping("/login")
    public RedirectView handleLogin(@RequestParam(value = "uuid", required = false) String uuid,
                                    RedirectAttributes attributes){
        // TODO Put some variables in properties file
        RandomString gen = new RandomString(8, ThreadLocalRandom.current());
        attributes.addAttribute("client_id", "1554235462");
        attributes.addAttribute("bot_prompt", "aggressive");
        attributes.addAttribute("scope", "openid profile");
        attributes.addAttribute("response_type", "code");
        attributes.addAttribute("redirect_uri", "https://line-handler-test.scoutout.net/auth");
        attributes.addAttribute("state", new RandomString(8, ThreadLocalRandom.current()).nextString());
        if (null != uuid) {
            attributes.addAttribute("nonce", uuid);
        }
        return new RedirectView("https://access.line.me/oauth2/v2.1/authorize");
    }

    @GetMapping("/auth")
    public RedirectView handleAuth(@RequestParam(value = "friendship_status_changed", required = false) Boolean friendshipStatusChanged,
                                        @RequestParam(value = "code", required = false) String authCode,
                                        @RequestParam(value = "state", required = false) String state,
                                        @RequestParam(value = "error", required = false) String errorCode,
                                        @RequestParam(value = "error_description", required = false) String errorDesc,
                                   RedirectAttributes redirectAttributes) throws MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append("friendship_status_changed=").append(friendshipStatusChanged);
        sb.append(" code=").append(authCode);
        sb.append(" state=").append(state);
        if (null != errorCode) {
            sb.append(" error=").append(errorCode);
        }
        if (null != errorDesc) {
            sb.append("error_description").append(errorDesc);
        }
        logger.debug(sb.toString());
        if (null != authCode) {
            ResponseEntity<String> response = exchangeAuthCodeForAccessToken(authCode);
            LineLoginBean loginBean = extractLineLoginResponse(response);
            logger.debug("LoginBean = {}", loginBean.toString());
            redirectAttributes.addAttribute("lineToken", loginBean.getAccessToken());

            // TODO We should just return LineLoginBean to client, and client handle it.
            if (null != loginBean.getUuid() && !loginBean.getUuid().isEmpty()) {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> akelaResponse = restTemplate.getForEntity("https://test.scoutout.net/api/v1.0/user/"+loginBean.getUuid()+"/line/"+loginBean.getLineUserId()+"/accesstoken/"+loginBean.getAccessToken(), String.class);
                JsonObject userModel = new Gson().fromJson(akelaResponse.getBody(), JsonObject.class);
                if (akelaResponse.getStatusCode().equals(HttpStatus.OK)) {
                    logger.debug("SUCCESSFULLY LINKED {} {} UUID {} WITH  LINE USER ID {}",
                            userModel.get("firstName").getAsString(),
                            userModel.get("lastName").getAsString(),
                            loginBean.getUuid(),
                            loginBean.getLineUserId());
                }
            }
        }
        return new RedirectView("https://scoutout-on-line.scoutout.net");
    }

    @GetMapping("/revoke")
    public ResponseEntity<?> handleRevoke(@RequestParam(value = "access_token", required = false) String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("access_token", accessToken);
        map.add("client_id", "1554235462");
        map.add("client_secret", "442ee71610d23a1935c9465a3df2e56f");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity( "https://api.line.me/oauth2/v2.1/revoke", request , String.class );
    }

    @GetMapping("/verify")
    public ResponseEntity<?> handleVerify(@RequestParam(value = "access_token", required = false) String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity( "https://api.line.me/oauth2/v2.1/verify?access_token="+accessToken, String.class);
    }

    private ResponseEntity<String> exchangeAuthCodeForAccessToken(@RequestParam(value = "code", required = false) String authCode) {
        logger.debug("Exchanging authCode for accessToken....");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        // TODO Put some variable in properties file
        map.add("grant_type", "authorization_code");
        map.add("code", authCode);
        map.add("redirect_uri", "https://line-handler-test.scoutout.net/auth");
        map.add("client_id", "1554235462");
        map.add("client_secret", "442ee71610d23a1935c9465a3df2e56f");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( "https://api.line.me/oauth2/v2.1/token", request , String.class );
        logger.debug("Got response code {} from https://api.line.me/oauth2/v2.1/token" , response.getStatusCode().toString());
        return response;
    }

    private LineLoginBean extractLineLoginResponse(ResponseEntity<String> response) throws MalformedURLException {
        JsonObject accessTokenResponse = new Gson().fromJson(response.getBody(), JsonObject.class);
        DecodedJWT jwt = JWT.decode(accessTokenResponse.get("id_token").getAsString());
        JsonObject jwtPayload = new Gson().fromJson(new String(Base64.getDecoder().decode(jwt.getPayload())), JsonObject.class);
        logger.debug("JWT Algorithm: {}", jwt.getAlgorithm());
        logger.debug("JWT Signature: {}", jwt.getSignature());

        LineLoginBean loginBean = new LineLoginBean();
        loginBean.setAccessToken(accessTokenResponse.get("access_token").getAsString());
        loginBean.setRefreshToken(accessTokenResponse.get("refresh_token").getAsString());
        loginBean.setScope(accessTokenResponse.get("scope").getAsString());
        loginBean.setTokenType(accessTokenResponse.get("token_type").getAsString());
        loginBean.setJwtIss(jwtPayload.get("iss").getAsString());
        loginBean.setLineUserId(jwtPayload.get("sub").getAsString());
        loginBean.setChannelId(jwtPayload.get("aud").getAsString());
        loginBean.setName(jwtPayload.get("name").getAsString());
        loginBean.setPictureUrl(new URL(jwtPayload.get("picture").getAsString()));
        if (null != jwtPayload.get("nonce")) {
            loginBean.setUuid(jwtPayload.get("nonce").getAsString());
        }
        return loginBean;
    }
}
