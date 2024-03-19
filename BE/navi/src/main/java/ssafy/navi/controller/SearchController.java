package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ssafy.navi.dto.CoverDto;
import ssafy.navi.dto.NoraebangDto;
import ssafy.navi.dto.Response;
import ssafy.navi.entity.Noraebang;
import ssafy.navi.service.CoverService;
import ssafy.navi.service.NoraebangService;
import ssafy.navi.service.SearchService;
import ssafy.navi.service.UserService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
        private final SearchService searchService;
        /*
        통합검색하기 Map형식으로
        쿼리스트링 형태로 keyword를 같이 넘겨주고
        keyword값을 이용하여 조회후 값을 가져오게 됨
        노래방 리스트, 커버 리스트, 유저 리스트 3개씩 조회함
        단 통합 검색이기 때문에 검색 카테고리 별 3개씩만 가져오게 됨
        PostMan 완
         */
        @GetMapping("")
        public Response<Map<String,Object>> serachAll(@RequestParam("keyword") String keyword) throws Exception{
                return Response.of("OK","통합 검색",searchService.getSearchAll(keyword));
        }

        /*
        노래방 더보기 제목
        일단 전체 조회 할게요
        쿼리스트링 형태로 keyword를 같이 넘겨주고
        keyword값을 이용하여 조회후 값을 가져오게 됨
        PostMan 완
         */
        @GetMapping("/noraebang/title")
        public Response<List<NoraebangDto>> searchMoreNoraebangTitle(@RequestParam("keyword") String keyword) throws Exception{
                return Response.of("OK","노래방 제목 더보기",searchService.getSearchMoreNoraebangTitle(keyword));
        }

        /*
        노래방 더보기 원곡자
        쿼리스트링 형태로 keyword 값으로 조회함
        PostMan 완
         */
        @GetMapping("/noraebang/artist")
        public Response<List<NoraebangDto>> searchMoreNoraebangArtist(@RequestParam("keyword") String keyword) throws Exception{
                return Response.of("OK","노래방 원곡자 더보기",searchService.getSearchMoreNoraebangArtist(keyword));
        }

        /*
        커버 더보기 제목
        일단 전체 조회 할게요
        쿼리스트링 형태로 keyword를 같이 넘겨주고
        keyword값을 이용하여 조회후 값을 가져오게 됨
        PostMan 완
         */
        @GetMapping("/cover/title")
        public Response<List<CoverDto>> searchMoreCoverTitle(@RequestParam("keyword") String keyword) throws Exception{
               return Response.of("OK","커버 제목 더보기",searchService.getSearchMoreCoverTitle(keyword));
        }

        /*
        커버 더보기 원곡자
        쿼리스트링 keyword
        PostMan 완
         */
        @GetMapping("/cover/artist")
        public Response<List<CoverDto>> searchMoreCoverArtist(@RequestParam("keyword") String keyword) throws Exception{
                return Response.of("OK","커버 원곡자 더보기",searchService.getSearchMoreCoverArtist(keyword));
        }

}
