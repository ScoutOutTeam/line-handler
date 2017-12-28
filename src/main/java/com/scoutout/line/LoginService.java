package com.scoutout.line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping
public class LoginService {
    private static final Logger logger = LoggerFactory
            .getLogger(LoginService.class);

    @GetMapping("/login")
    public RedirectView handleLogin(RedirectAttributes attributes){
        RandomString gen = new RandomString(8, ThreadLocalRandom.current());
        attributes.addAttribute("client_id", "1554235462");
        attributes.addAttribute("bot_prompt", "normal");
        attributes.addAttribute("scope", "openid profile");
        attributes.addAttribute("response_type", "code");
        attributes.addAttribute("redirect_uri", "https://line-handler-test.scoutout.net/auth");
        attributes.addAttribute("state", new RandomString(8, ThreadLocalRandom.current()).nextString());
        return new RedirectView("https://access.line.me/oauth2/v2.1/authorize");
    }

    @GetMapping("/auth")
    public ResponseEntity<?> handleAuth(@RequestParam(value = "friendship_status_changed", required = false) Boolean friendshipStatusChanged,
                                        @RequestParam(value = "code", required = false) String authCode,
                                        @RequestParam(value = "state", required = false) String state,
                                        @RequestParam(value = "error", required = false) String errorCode,
                                        @RequestParam(value = "error_description", required = false) String errorDesc) {
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
        logger.debug("Got Login Callback from LINE : {}", sb.toString());
        return new ResponseEntity<>(sb.toString(), HttpStatus.OK);

    }
}
