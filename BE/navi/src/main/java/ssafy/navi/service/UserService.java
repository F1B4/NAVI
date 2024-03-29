package ssafy.navi.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.user.*;
import ssafy.navi.entity.cover.Cover;
import ssafy.navi.entity.cover.CoverUser;
import ssafy.navi.entity.user.Follow;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final CoverUserRepository coverUserRepository;
    private final CoverRepository coverRepository;
    private final S3Service s3Service;
    private final FollowRepository followRepository;
    private final NotificationService notificationService;
    private final NoraebangRepository noraebangRepository;

    /*
    쿠키 삭제
     */
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        // Authorization 쿠키 삭제
        Cookie authorization = new Cookie("Authorization", null); // 쿠키를 생성하고 값은 null로 설정
        authorization.setPath("/"); // 쿠키의 경로 설정
        authorization.setMaxAge(0); // 쿠키의 유효 시간을 0으로 설정하여 바로 만료
        response.addCookie(authorization); // 응답에 쿠키 추가하여 클라이언트에 전송, 쿠키 삭제 지시

        // JSESSIONID 쿠키 수동 무효화
        Cookie jsessionid = new Cookie("JSESSIONID", null);
        jsessionid.setPath(request.getContextPath());
        jsessionid.setMaxAge(0);
        response.addCookie(jsessionid);

        // 세션 무효화
        request.getSession().invalidate();

    }

    /*
    인가된 토큰에서 유저 정보 획득
     */
    public UserDto getUserInfo() throws Exception {
        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        return UserDto.convertToDto(user);
    }

    public UserProfileDto getUserProfile(Long userPk) throws Exception {
        User user = userRepository.findById(userPk)
                .orElseThrow(() -> new Exception("유저가 존재하지 않음"));
        // 해당 유저의 중계 테이블 모두 조회
        List<CoverUser> coverUsers = coverUserRepository.findByUserId(user.getId());
        Set<Long> coverPks = coverUsers.stream()
                .map(coverUser -> coverUser.getCover().getId())
                .collect(Collectors.toSet());
        // 중복 없는 CoverPk를 사용하여 Cover 객체들을 조회
        List<Cover> covers = coverRepository.findAllById(coverPks);

        return UserProfileDto.convertToDto(user, covers);
    }

    /*
    프로필 이미지 수정
    UserDto
     */
    @Transactional
    public UserDto updateUserImage(MultipartFile file) throws Exception {
        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        // 해당 유저의 예전 프로필 사진을 S3에서 삭제
        String oldFileNmae = user.getImage();
        s3Service.deleteImage(oldFileNmae);

        // 파일을 S3로 저장 후, 프로필 사진 업데이트
        String fileName = s3Service.saveFile(file);
        user.updateImage(fileName);

        return UserDto.convertToDto(user);
    }

    /*
    닉네임 수정
    UserDto
     */
    @Transactional
    public UserDto updateUserNickname(String nickname) throws Exception {
        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        user.updateNickname(nickname);

        return UserDto.convertToDto(user);
    }

    /*
    유저 팔로우/언팔로우
    followingDto
     */
    @Transactional
    public FollowingDto follow(Long userPk) throws Exception{
        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User fromUser = userRepository.findByUsername(customOAuth2User.getUsername());

        User toUser = userRepository.findById(userPk)
                .orElseThrow(() -> new Exception("팔로우 할 유저가 존재하지 않음"));

        Follow follow = followRepository.findByFromUserIdAndToUserId(fromUser.getId(),toUser.getId());

        // 이미 팔로우하고 있을 경우, 언팔로우 후 카운트 조정
        if (follow!=null) {
            // 언팔로우
            followRepository.delete(follow);
            // 카운트 조정
            fromUser.updateFollowingCount(-1);
            toUser.updateFollowerCount(-1);
            // 여기서 toUser에게(내가 팔로우 하는 상대방) 언팔로우 메세지 보내기

            return null;
        }
        // 팔로우하고 있지 않을 경우, 팔로우 후 카운트 조정
        else {
            // 팔로우
            follow = Follow.builder()
                    .fromUser(fromUser)
                    .toUser(toUser)
                    .build();
            followRepository.save(follow);
            // 카운트 조정
            fromUser.updateFollowingCount(1);
            toUser.updateFollowerCount(1);
            // 여기서 toUser에게(내가 팔로우 하는 상대방) 팔로우 메세지 보내기
            String s = fromUser.getNickname() + "님이 팔로우 하셨습니다.";
            notificationService.sendNotificationToUser(toUser.getId(), s);
            return FollowingDto.convertToDto(follow);
        }
    }

    /*
    유저 팔로잉 리스트 조회
    followingDto
     */
    public List<FollowingDto> getFollowingList(Long userPk) throws Exception {
        // 팔로잉 리스트 조회할 유저 검색
        User fromUser = userRepository.findById(userPk)
                .orElseThrow(() -> new Exception("유저가 존재하지 않음"));

        // 해당 유저의 팔로잉 정보 조회
        List<Follow> follows = followRepository.findAllByFromUserId(fromUser.getId());

        return follows.stream()
                .map(FollowingDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    유저 팔로워 리스트 조회
    followerDto
     */
    public List<FollowerDto> getFollowerList(Long userPk) throws Exception{
        // 팔로워 리스트 조회할 유저 검색
        User toUser = userRepository.findById(userPk)
                .orElseThrow(() -> new Exception("유저가 존재하지 않음"));

        // 해당 유저의 팔로잉 정보 조회
        List<Follow> follows = followRepository.findAllByToUserId(toUser.getId());

        return follows.stream()
                .map(FollowerDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    유저 녹음된 목소리 파일 개수 조회
    Integer
     */
    public Integer getUserVoiceCount() throws Exception{
        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        return noraebangRepository.countByUserId(user.getId());
    }
}
