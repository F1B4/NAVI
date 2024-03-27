package ssafy.navi.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class fastAPIController {
    private final WebClient webClient = WebClient.create();

    @GetMapping("/fastapi")
    public Mono<String> sendRequest() {
        String url = "https://j10d107.p.ssafy.io/media/ss";
        return webClient.get().uri(url).retrieve().bodyToMono(String.class);
    }
}
