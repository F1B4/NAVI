package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.user.*;
import ssafy.navi.entity.user.Role;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // DefaultOAuth2UserService OAuth2UserService의 구현체

    private final UserRepository userRepository;

    /*
    OAuth2UserRequest : 리소스 정보에서 제공해주는 유저 정보
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 서비스별 Resopnse 세팅
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        User existData = userRepository.findByUsername(username);
        Role role = Role.ROLE_GUEST;

        // 회원가입
        if(existData==null) {

            User user = User.builder()
                    .username(username)
                    .nickname(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .image(oAuth2Response.getProfileImage())
                    .role(role)
                    .build();

            userRepository.save(user);

            UserDto userDto = UserDto.convertToDto(user);
            return new CustomOAuth2User(userDto);
        }
        // 이미 회원가입이 되어있을 경우, 소셜로그인 일부 정보 재설정
        else {
            existData.updateEmail(oAuth2Response.getEmail());

            userRepository.save(existData);

            UserDto userDto = UserDto.convertToDto(existData);
            return new CustomOAuth2User(userDto);
        }
    }
}
