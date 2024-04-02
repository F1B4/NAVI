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
                .allowedOrigins("http://localhost:5173","http://navi.iptime.org:8085","https://j10d107.p.ssafy.io");
//                .allowedOrigins("http://navi.iptime.org:8085");

//                .allowedOrigins("https://j10d107.p.ssafy.io");
        //서버용
    }
}
