package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    /*
    인가된 토큰에서 유저 정보 획득
    UserDto 이용
     */
    @GetMapping("/info")
    public Response<UserDto> getInfo() throws Exception {
        return Response.of("OK", "프로필 조회 성공",userService.getUserInfo());
    }

}
