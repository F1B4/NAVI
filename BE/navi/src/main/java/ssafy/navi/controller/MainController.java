package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.navi.dto.CoverDto;
import ssafy.navi.dto.Response;
import ssafy.navi.service.CoverService;
import ssafy.navi.service.NoraebangService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final CoverService coverService;
    private final NoraebangService noraebangService;

    /*
    최신 컨텐츠 10개 가져오기
     */
    


    /*
    HOT 커버 게시글 목록 가져오기
    최근 1주일간 조회수를 기준으로 조회수가 가장 높은 6개의 게시글을 가져옴
    PostMan 완
     */
    @GetMapping("/covers/hot")
    public Response<List<CoverDto>> getHotCover() throws Exception{
        return Response.of("OK","Hot 게시글 가져오기",coverService.getHotCover());
    }

}
