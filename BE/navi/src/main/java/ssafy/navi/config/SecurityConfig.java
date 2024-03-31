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
                .requestMatchers("/**")
//                // UserController
//                .requestMatchers("/users/following/**")
//                .requestMatchers("/users/follower/**")
//                // MainController
//                .requestMatchers("/main/**")
//                // CoverController
//                .requestMatchers("/covers")
//                .requestMatchers("/covers/byView")
//                .requestMatchers("/covers/byLike")
//                // NoraebangController
//                .requestMatchers("/noraebangs")
//                .requestMatchers("/noraebangs/**")
////                .requestMatchers("/noraebangs/byView")
////                .requestMatchers("/noraebangs/byLike")
//                // NotificationController
//                .requestMatchers("/sse/notification/**")
//                // fastAPIController
//                .requestMatchers("/ai/**")
//                .requestMatchers("/ai/cover")
//                .requestMatchers("/ai/train")
//                // alarmController
//                .requestMatchers("/alarms/**")
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
//                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
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

        //경로별 인가 작업
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