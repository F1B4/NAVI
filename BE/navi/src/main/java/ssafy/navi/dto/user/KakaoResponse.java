package ssafy.navi.dto.user;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    // kakao는 kakao_account에 유저정보가 존재 (email)
    private Map<String, Object> kakaoAccount = (Map<String, Object>)attribute.get("kakao_account");
    // kakao_account안에 또 profile이라는 JSON객체가 존재 (nickname, profile_image)
    private Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        return kakaoProfile.get("nickname").toString();
    }

    @Override
    public String getProfileImage() {
        return kakaoProfile.get("profile_image_url").toString();
    }
}
