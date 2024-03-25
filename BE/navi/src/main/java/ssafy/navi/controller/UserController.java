package ssafy.navi.controller;

import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ssafy.navi.dto.user.CustomOAuth2User;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.UserRepository;

import java.security.Security;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/info")
    public String getInfo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User user = (CustomOAuth2User)authentication.getPrincipal();
        System.out.println(user.getUsername());
        return user.getUsername();
    }

}
