package ssafy.navi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FastApiService {

    private final WebClient webClient;

    public FastApiService() {
        this.webClient = WebClient.create("http://navi.iptime.org:8085"); // FastAPI 서버의 URL을 여기에 입력합니다.
    }

    public Mono<String> fetchDataFromFastAPI(String endPoint, Long pk) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(endPoint)
                        .queryParam("pk", pk) // 사용자 PK를 쿼리 매개변수로 추가합니다.
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

}
