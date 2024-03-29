package ssafy.navi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.user.FollowerDto;
import ssafy.navi.dto.user.FollowingDto;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.dto.user.UserProfileDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    /*
    로그아웃 (쿠키 삭제)
     */
    @GetMapping("/logout")
    public Response<String> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.deleteCookie(request, response);
        return Response.of("OK", "로그아웃 성공", "");
    }

    /*
    토큰에서 유저 정보 획득
    UserDto
     */
    @GetMapping("/info")
    public Response<UserDto> getInfo() throws Exception {
        return Response.of("OK", "유저 정보 조회 성공",userService.getUserInfo());
    }

    /*
    UserPk으로 유저 프로필 조회
    UserProfileDto
     */
    @GetMapping("/profile/{user_pk}")
    public Response<UserProfileDto> getUserProfile(@PathVariable("user_pk") Long userPk) throws Exception {
        return Response.of("OK", "유저 프로필 조회 성공",userService.getUserProfile(userPk));
    }

    /*
    프로필 이미지 수정
    UserDto
     */
    @PostMapping("/image")
    public Response<UserDto> updateUserImage(@RequestBody MultipartFile file) throws Exception{
        return Response.of("OK", "유저 프로필 사진 수정 성공", userService.updateUserImage(file));
    }

    /*
    닉네임 수정
    UserDto
     */
    @PostMapping("/nickname")
    public Response<UserDto> updateUserNickname(@RequestBody String nickname) throws Exception {
        return Response.of("OK", "유저 닉네임 수정 성공", userService.updateUserNickname(nickname));
    }

    /*
    유저 팔로우/언팔로우
    followingDto
     */
    @PostMapping("/follow/{user_pk}")
    public Response<FollowingDto> follow(@PathVariable("user_pk") Long userPk) throws Exception {
        FollowingDto res = userService.follow(userPk); //내가 userPk에게 팔로우 거는거
        if(res!=null) {
            return Response.of("OK","유저 팔로우 성공", res);
        } else {
            return Response.of("OK","유저 언팔로우 성공", null);
        }
    }

    /*
    유저 팔로잉 리스트 조회
    followingDto
     */
    @GetMapping("/following/{user_pk}")
    public Response<List<FollowingDto>> getFollowingList(@PathVariable("user_pk") Long userPk) throws Exception {
        return Response.of("OK", "유저 팔로잉 리스트 조회 성공", userService.getFollowingList(userPk));
    }

    /*
    유저 팔로워 리스트 조회
    followerDto
     */
    @GetMapping("/follower/{user_pk}")
    public Response<List<FollowerDto>> getFollowerList(@PathVariable("user_pk") Long userPk) throws Exception {
        return Response.of("OK", "유저 팔로워 리스트 조회 성공", userService.getFollowerList(userPk));
    }

}
