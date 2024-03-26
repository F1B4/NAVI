package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.user.CustomOAuth2User;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.dto.user.UserInfoDto;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /*
    인가된 토큰에서 유저 정보 획득
     */
    public UserDto getUserInfo() throws Exception{
        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        return UserDto.convertToDto(user);
    }

    public User findById(Long userPk) throws Exception {
        Optional<User> user = userRepository.findById(userPk);
        return user.orElseThrow(() -> new Exception("유저를 찾을 수 없습니다."));
    }
}
