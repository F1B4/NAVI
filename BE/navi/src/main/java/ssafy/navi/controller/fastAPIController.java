package ssafy.navi.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fastapi")
@Slf4j
public class fastAPIController {
    private final WebClient webClient = WebClient.create();

    @GetMapping("")
    public Mono<String> sendRequest() {
        String url = "https://j10d107.p.ssafy.io/media/ss";
        return webClient.get().uri(url).retrieve().bodyToMono(String.class);
    }
    
}
