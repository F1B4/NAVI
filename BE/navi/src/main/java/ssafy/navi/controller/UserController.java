package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.dto.user.UserProfileDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    /*
    토큰에서 유저 정보 획득
    UserDto 이용
     */
    @GetMapping("/info")
    public Response<UserDto> getInfo() throws Exception {
        return Response.of("OK", "유저 정보 조회 성공",userService.getUserInfo());
    }

    /*
    UserPk으로 유저 프로필 조회
    UserProfileDto 이용
     */
    @GetMapping("/profile/{user_pk}")
    public Response<UserProfileDto> getUserProfile(@PathVariable("user_pk") Long userPk) throws Exception {
        return Response.of("OK", "유저 프로필 조회 성공",userService.getUserProfile(userPk));
    }

}
