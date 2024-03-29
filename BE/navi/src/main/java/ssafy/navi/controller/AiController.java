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
@RequestMapping("/ai")
@Slf4j
public class AiController {
    private final WebClient webClient = WebClient.create();

    @GetMapping("/cover") //커버 생성 완료
    public String complete() {
        System.out.println("=================oj---===========");
        return "OK";
    }

    @GetMapping("/train")
    public Mono<String> sendRequest() {
        String url = "https://j10d107.p.ssafy.io/media/ss";
        return webClient.get().uri(url).retrieve().bodyToMono(String.class);
    }
}
