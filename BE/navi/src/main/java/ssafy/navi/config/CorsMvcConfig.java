package ssafy.navi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins("https://j10d107.p.ssafy.io", "http://navi.iptime.org:8085", "http://localhost:5173")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }
}
