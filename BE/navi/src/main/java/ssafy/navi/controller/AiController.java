package ssafy.navi.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ssafy.navi.dto.util.Response;
import ssafy.navi.service.CoverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
@Slf4j
public class AiController {

    private final CoverService coverService;

    @PostMapping("/cover/{cover_pk}") //커버 생성 완료
    public Response<?> complete(@PathVariable("cover_pk") Long coverPk) throws Exception {
        //cover에서 coverUser뽑아서 noti보내기
        coverService.completeCoverVideo(coverPk);
        return Response.of("OK", "커버 영상 제작 완료, 유저에게 알람 보내기 성공", null);
    }

    @PostMapping("/train/{user_pk}")
    public Response<?> train(@PathVariable("user_pk") Long userPk) throws Exception {
        coverService.completeTrain(userPk);
        System.out.println("userPk =~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ " + userPk);
        return Response.of("OK", "커버 영상 제작 완료, 유저에게 알람 보내기 성공", null);
    }
}
