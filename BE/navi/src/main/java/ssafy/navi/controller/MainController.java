package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ssafy.navi.dto.cover.CoverDto;
import ssafy.navi.dto.noraebang.NoraebangAllDto;
import ssafy.navi.dto.noraebang.NoraebangDto;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.dto.util.TimeDto;
import ssafy.navi.service.CoverService;
import ssafy.navi.service.MainService;
import ssafy.navi.service.NoraebangService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
@Slf4j
public class MainController {
    private final CoverService coverService;
    private final NoraebangService noraebangService;
    private final MainService mainService;

    /*
    최신 컨텐츠 10개 가져오기
     */
    @GetMapping("/new")
    public Response<List<TimeDto>> getNewContents() throws Exception{
        return Response.of("OK","최신 컨텐츠 가져오기",mainService.getNewContents());
    }

    @GetMapping("/test")
    public void ssss() {
        
    }
    /*
    HOT 노래방 게시글 목록 가져오기
    최근 1주일간 조회수를 기준으로 조회수가 가장 높은 6개의 게시글을 가져옴
     */
    @GetMapping("/noraebangs/hot")
    public Response<List<NoraebangAllDto>> getHotNoraebang() throws Exception{
        return Response.of("OK","Hot 게시글 가져오기",mainService.getHotNoraebang());
    }

    /*
    HOT 커버 게시글 목록 가져오기
    최근 1주일간 조회수를 기준으로 조회수가 가장 높은 6개의 게시글을 가져옴
     */
    @GetMapping("/covers/hot")
    public Response<List<CoverDto>> getHotCover() throws Exception{
        return Response.of("OK","Hot 게시글 가져오기",mainService.getHotCover());
    }

    /*
    통합검색하기 Map형식으로
    쿼리스트링 형태로 keyword를 같이 넘겨주고
    keyword값을 이용하여 조회후 값을 가져오게 됨
    노래방 리스트, 커버 리스트, 유저 리스트 3개씩 조회함
    단 통합 검색이기 때문에 검색 카테고리 별 3개씩만 가져오게 됨
     */
    @GetMapping("")
    public Response<Map<String,Object>> serachAll(@RequestParam("keyword") String keyword) throws Exception{
        return Response.of("OK","통합 검색",mainService.getSearchAll(keyword));
    }

    /*
    노래방 더보기 제목
    일단 전체 조회 할게요
    쿼리스트링 형태로 keyword를 같이 넘겨주고
    keyword값을 이용하여 조회후 값을 가져오게 됨
     */
    @GetMapping("/noraebang/title")
    public Response<List<NoraebangDto>> searchMoreNoraebangTitle(@RequestParam("keyword") String keyword) throws Exception{
        return Response.of("OK","노래방 제목 더보기",mainService.getSearchMoreNoraebangTitle(keyword));
    }

    /*
    노래방 더보기 원곡자
    쿼리스트링 형태로 keyword 값으로 조회함
     */
    @GetMapping("/noraebang/artist")
    public Response<List<NoraebangDto>> searchMoreNoraebangArtist(@RequestParam("keyword") String keyword) throws Exception{
        return Response.of("OK","노래방 원곡자 더보기",mainService.getSearchMoreNoraebangArtist(keyword));
    }

    /*
    커버 더보기 제목
    일단 전체 조회 할게요
    쿼리스트링 형태로 keyword를 같이 넘겨주고
    keyword값을 이용하여 조회후 값을 가져오게 됨
     */
    @GetMapping("/cover/title")
    public Response<List<CoverDto>> searchMoreCoverTitle(@RequestParam("keyword") String keyword) throws Exception{
        return Response.of("OK","커버 제목 더보기",mainService.getSearchMoreCoverTitle(keyword));
    }

    /*
    커버 더보기 원곡자
    쿼리스트링 keyword
     */
    @GetMapping("/cover/artist")
    public Response<List<CoverDto>> searchMoreCoverArtist(@RequestParam("keyword") String keyword) throws Exception{
        return Response.of("OK","커버 원곡자 더보기",mainService.getSearchMoreCoverArtist(keyword));
    }

    /*
    사용자 더보기
    쿼리스트링 keyword
     */
    @GetMapping("/user")
    public Response<List<UserDto>> searchMoreUser(@RequestParam("keyword") String keyword) throws Exception{
        return Response.of("OK","유저 닉네임 더보기",mainService.getSearchMoreUser(keyword));
    }
}
