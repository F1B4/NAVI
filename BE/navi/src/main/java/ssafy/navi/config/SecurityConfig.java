package ssafy.navi.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import ssafy.navi.jwt.JWTFilter;
import ssafy.navi.jwt.JWTUtil;
import ssafy.navi.oauth2.CustomClientRegistrationRepository;
import ssafy.navi.oauth2.CustomSuccessHandler;
import ssafy.navi.service.CustomOAuth2UserService;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomClientRegistrationRepository customClientRegistrationRepository;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    // 시큐리티 무시하는 URI 추가
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/users/following/{user_pk}")
                .requestMatchers("/users/follower/{user_pk}")
                .requestMatchers("/users/profile/{user_pk}/{login_user_pk}")
                // MainController
                .requestMatchers("/main/new")
                .requestMatchers("/main/noraebangs/hot")
                .requestMatchers("/main/covers/hot")
                .requestMatchers("/main")
                .requestMatchers("/main/noraebang/title/{keyword}")
                .requestMatchers("/main/noraebang/artist/{keyword}")
                .requestMatchers("/main/cover/title/{keyword}")
                .requestMatchers("/main/cover/artist/{keyword}")
                .requestMatchers("/main/user/{keyword}")
                // CoverController
                .requestMatchers("/covers")
                .requestMatchers("/covers/create")
                .requestMatchers("/covers/byView")
                .requestMatchers("/covers/byLike")
                .requestMatchers("/covers/detail/{cover_pk}/{user_pk}")
                // NoraebangController
                .requestMatchers("/noraebangs")
                .requestMatchers("/noraebangs/byView")
                .requestMatchers("/noraebangs/byLike")
                .requestMatchers("/noraebangs/detail/{noraebang_pk}/{user_pk}")
                .requestMatchers("/noraebangs/complete/{noraebang_pk}")
                // NotificationController
                .requestMatchers("/sse/notification/subscribe/{userId}")
                // ai
                .requestMatchers("ai/cover/{cover_pk}")
                .requestMatchers("ai/train/{user_pk}")
                ;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("https://j10d107.p.ssafy.io"));
                        configuration.setAllowedMethods(Arrays.asList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Arrays.asList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));

                        return configuration;
                    }
                }));

        //csrf disable
        http
                .csrf((csrf) -> csrf.disable());

        //Form 로그인 방식 disable
        http
                .formLogin((login) -> login.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((basic) -> basic.disable());

        //JWTFilter 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .clientRegistrationRepository(customClientRegistrationRepository.clientRegistrationRepository()) // 변수 설정 세팅
                        .userInfoEndpoint((userInfoEndpointConfig) ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );

//        경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated());




        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}