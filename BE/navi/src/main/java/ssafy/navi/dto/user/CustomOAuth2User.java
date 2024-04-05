package ssafy.navi.dto.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final UserDto userDto;

    // 서비스 별 사용하는 Attribute가 다르기 때문에 사용하지 않는다
    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            public String getAuthority() {
                return userDto.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDto.getNickname();
    }

    public String getUsername() {
        return userDto.getUsername();
    }

}
