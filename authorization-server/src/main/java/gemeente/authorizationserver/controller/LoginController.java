package gemeente.authorizationserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class LoginController {

    @Value("${content.logo:/resources/img/default-logo.svg}")
    private String pathLogo;

    @Value("${content.login.introduction:}")
    private String introduction;

    @GetMapping("/show-login")
    public String login(final Map<String, Object> model) {
        model.put("logo", pathLogo);
        model.put("introduction", introduction);
        return "login";
    }
}


