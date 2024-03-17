package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.navi.dto.CoverDto;
import ssafy.navi.dto.CoverLikeDto;
import ssafy.navi.dto.CoverReviewDto;
import ssafy.navi.dto.Response;
import ssafy.navi.entity.CoverReview;
import ssafy.navi.service.CoverService;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/covers")
public class CoverController {

    private final CoverService coverService;


    /*
    커버 게시판 목록 가져오기
     */
//    @GetMapping("/")
//    public Response<Map<String,Object>> getCover() throws Exception{
//        return Response.of("OK","게시글 목록 가져오기",coverService.getCover());
//    }

    /*
    커버 게시판 디테일 보기, pathvariable로 온 cover_pk를 통해 조회해서 Map형식으로 필요한 정보를 클라이언트로 보냄
    커버 정보, 커버 댓글, 커버 좋아요, 원곡 정보, 맡은 파트
    PostMan 완
     */
    @GetMapping("/{cover_pk}")
    public Response<Map<String, Object>> getCoverDetail(@PathVariable("cover_pk") Long coverPk) throws Exception {
        return Response.of("OK","게시글 상세보기",coverService.getCoverDetail(coverPk));
    }

    /*
    커버 게시판 댓글 작성
    클라이언트에서 댓글 작성자의 정보를 어떻게 넘겨주냐에 따라 바뀔예정
    RequestBody에 내용을 받아옴
    PostMan 완
     */
    @PostMapping("/{cover_pk}/review")
    public Response<CoverReviewDto> createCoverReview(@PathVariable("cover_pk") Long coverPk, @RequestBody CoverReviewDto coverReviewDto) throws Exception {
        return Response.of("OK","댓글 작성",coverService.createCoverReview(coverPk, coverReviewDto));
    }

    /*
    커버 게시판 댓글 삭제
    클라이언트에서 댓글 작성자의 정보와 로그인한 유저의 정보가 일치할 때만 삭제할 수 있도록 하기
    게시글 정보와 유저 정보 받아와서 처리하기
    PostMan 완
     */
    @DeleteMapping("/{cover_pk}/review/{cover_review_pk}")
    public Response<String> deleteCoverReview(@PathVariable("cover_pk")Long coverPk,@PathVariable("cover_review_pk")Long coverReviewPk) throws Exception{
        return Response.of("OK","댓글 삭제",coverService.deleteCoverReview(coverPk,coverReviewPk));
    }

    /*
    커버 게시글 좋아요

     */
    @PostMapping("/{cover_pk}/like")
    public Response<CoverLikeDto> coverLike(@PathVariable("cover_pk") Long coverPk) throws Exception{
        CoverLikeDto res=coverService.coverLike(coverPk);
        if(res!=null){
            return Response.of("OK","좋아요 성공",res);
        }else{
            return Response.of("OK","좋아요 삭제",null);
        }
    }
}