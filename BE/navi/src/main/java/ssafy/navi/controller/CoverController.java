package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ssafy.navi.dto.cover.CoverDto;
import ssafy.navi.dto.cover.CoverLikeDto;
import ssafy.navi.dto.cover.CoverRegistDto;
import ssafy.navi.dto.cover.CoverReviewDto;
import ssafy.navi.dto.song.ArtistDto;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.service.ArtistService;
import ssafy.navi.service.CoverService;
import ssafy.navi.service.SongService;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/covers")
@Slf4j
public class CoverController {
    private final CoverService coverService;
    private final ArtistService artistService;
    private final SongService songService;
    /*
    커버 게시판 목록 가져오기
    최신순
     */
    @GetMapping("")
    public Response<List<CoverDto>> getCover() throws Exception{
        return Response.of("OK","게시글 목록 가져오기",coverService.getCover());
    }

    /*
    커버 게시판 목록 가져오기
    조회수 순
     */
    @GetMapping("/byView")
    public Response<List<CoverDto>> getCoverByView() throws Exception{
        return Response.of("OK","게시글 목록 조회순으로 가져오기",coverService.getCoverByView());
    }

    /*
    커버 게시판 정렬하기
    좋아요 순
     */
    @GetMapping("/byLike")
    public Response<List<CoverDto>> getCoverByLike() throws Exception{
        return Response.of("OK","게시글 목록 좋아요 순으로 가져오기",coverService.getCoverByLike());
    }

    /*
    아티스트 정보 가져오기
    커버 생성 화면으로 갔을 때 처음엔 아티스트 정보들을 보내줘야 아티스트를 선택할 수 있기 때문에 아티스트를 담아서 보냄
     */
    @GetMapping("/info")
    public Response<List<ArtistDto>> getArtist() throws Exception{
        return Response.of("OK","아티스트 정보 전부 가져오기",artistService.getAllArtist());
    }

    /*
    곡 목록 가져오기
    아티스트 선택 후 해당 아티스트의 곡들을 받아야함
     */
    @GetMapping("/{artist_pk}/song")
    public Response<List<SongDto>> getSong(@PathVariable("artist_pk") Long artistPk) throws Exception{
        return Response.of("OK","아티스트의 노래 전부 가져오기",songService.getSongByArtist(artistPk));
    }

    /*
    파트와 맞팔로우 목록 가져오기
     */
    @GetMapping("/{song_pk}/select")
    public Response<Map<String,Object>> getPartAndMutualFollow(@PathVariable("song_pk") Long songPk) throws Exception{
        return Response.of("OK","파트 및 맞팔로우 목록 가져오기",coverService.getPartAndMutualFollow(songPk));
    }

    /*
    매칭 요청하기
     */
    @PostMapping("")
    public Response<?> createCover(@RequestBody CoverRegistDto coverRegistDto) throws Exception{
        return Response.of("OK","Make a Song 시작하기",coverService.createCover(coverRegistDto));
    }

    /*
    커버 게시판 디테일 보기, pathvariable로 온 cover_pk를 통해 조회해서 Map형식으로 필요한 정보를 클라이언트로 보냄
    커버 정보, 커버 댓글, 커버 좋아요, 원곡 정보, 맡은 파트
     */
    @GetMapping("/detail/{cover_pk}")
    public Response<CoverDto> getCoverDetail(@PathVariable("cover_pk") Long coverPk) throws Exception {
        return Response.of("OK","게시글 상세보기",coverService.getCoverDetail(coverPk));
    }

    /*
    커버 게시판 댓글 작성
    클라이언트에서 댓글 작성자의 정보를 어떻게 넘겨주냐에 따라 바뀔예정
    RequestBody에 내용을 받아옴
     */
    @PostMapping("/{cover_pk}/reivew")
    public Response<CoverReviewDto> createCoverReview(@PathVariable("cover_pk") Long coverPk, @RequestBody CoverReviewDto coverReviewDto) throws Exception {
        return Response.of("OK","댓글 작성",coverService.createCoverReview(coverPk, coverReviewDto));
    }

    /*
    커버 게시판 댓글 삭제
    클라이언트에서 댓글 작성자의 정보와 로그인한 유저의 정보가 일치할 때만 삭제할 수 있도록 하기
    게시글 정보와 유저 정보 받아와서 처리하기
     */
    @DeleteMapping("/review/{cover_review_pk}")
    public Response<String> deleteCoverReview(@PathVariable("cover_review_pk")Long coverReviewPk) throws Exception{
        return Response.of("OK","댓글 삭제",coverService.deleteCoverReview(coverReviewPk));
    }

    /*
    커버 게시글 좋아요
     */
    @PostMapping("/{cover_pk}/like")
    public Response<CoverLikeDto> coverLike(@PathVariable("cover_pk") Long coverPk) throws Exception{
        CoverLikeDto res=coverService.toggleCoverLike(coverPk);
        if(res!=null){
            return Response.of("OK","좋아요 성공",res);
        }else{
            return Response.of("OK","좋아요 삭제",null);
        }
    }
}