package ssafy.navi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SocialLoginController {

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

}
