package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.dto.user.UserProfileDto;
import ssafy.navi.entity.cover.Cover;
import ssafy.navi.entity.cover.CoverUser;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.CoverRepository;
import ssafy.navi.repository.CoverUserRepository;
import ssafy.navi.repository.UserRepository;

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

    /*
    인가된 토큰에서 유저 정보 획득
     */
    public UserDto getUserInfo() throws Exception{
        // 현재 인가에서 유저 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
//        User user = userRepository.findByUsername(customOAuth2User.getUsername());
        //==테스트용임시정보==//
        User user = userRepository.findById(6L)
                .orElseThrow(() -> new Exception("유저가 존재하지 않음"));

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

    public User findById(Long userPk) throws Exception {
        Optional<User> user = userRepository.findById(userPk);
        return user.orElseThrow(() -> new Exception("유저를 찾을 수 없습니다."));
    }

    /*
    프로필 이미지 수정
    UserDto
     */
    @Transactional
    public UserDto updateUserImage(MultipartFile file) throws Exception {
        // 현재 인가에서 유저 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
//        User user = userRepository.findByUsername(customOAuth2User.getUsername());
        //==테스트용임시정보==//
        User user = userRepository.findById(6L)
                .orElseThrow(() -> new Exception("유저가 존재하지 않음"));

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
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
//        User user = userRepository.findByUsername(customOAuth2User.getUsername());
        //==테스트용임시정보==//
        User user = userRepository.findById(6L)
                .orElseThrow(() -> new Exception("유저가 존재하지 않음"));

        user.updateNickname(nickname);

        return UserDto.convertToDto(user);
    }
}
