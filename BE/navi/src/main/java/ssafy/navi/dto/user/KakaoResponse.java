package ssafy.navi.dto.user;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    // kakao는 kakao_account에 유저정보가 존재 (email)
    private final Map<String, Object> kakaoAccount;
    // kakao_account안에 또 profile이라는 JSON객체가 존재 (nickname, profile_image)
    private final Map<String, Object> kakaoProfile;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
        this.kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        this.kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
    }

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
