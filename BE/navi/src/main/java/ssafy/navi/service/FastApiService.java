package ssafy.navi.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FastApiService {

    private final WebClient webClient;

    public FastApiService() {
        this.webClient = WebClient.create("http://navi.iptime.org:8085"); // FastAPI 서버의 URL을 여기에 입력합니다.
    }

    public String fetchDataFromFastAPI(String endPoint, Long pkValue) {
        String pk = String.valueOf(pkValue);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("pk", pk);

        return webClient.post()
                .uri(endPoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String fetchDataFromFastAPI(String endPoint, Long pkValue, String path, Long noraebangPk) {
        String pk = String.valueOf(pkValue);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("pk", pk);
        formData.add("path", path);
        formData.add("noraebangPk", String.valueOf(noraebangPk));

        return webClient.post()
                .uri(endPoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
