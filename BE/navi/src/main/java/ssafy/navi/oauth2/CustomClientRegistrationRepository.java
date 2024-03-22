package ssafy.navi.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/*
ClientRegistration의 저장소
서비스별 ClientRegistration들을 가짐
 */
@Configuration
@RequiredArgsConstructor
public class CustomClientRegistrationRepository {
    private final SocialClientRegistration socialClientRegistration;

    // InMemoryClientRegistrationRepository 클래스를 사용하여 인메모리 방식으로 관리를 진행
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(socialClientRegistration.naverClientRegistration(), socialClientRegistration.googleClientRegistration());
    }
}
