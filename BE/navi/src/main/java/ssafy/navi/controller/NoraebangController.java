package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.noraebang.NoraebangReviewDto;
import ssafy.navi.dto.song.ArtistDto;
import ssafy.navi.dto.noraebang.NoraebangDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.service.ArtistService;
import ssafy.navi.service.NoraebangService;
import ssafy.navi.service.S3Service;
import ssafy.navi.service.SongService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/noraebangs")
@Slf4j
public class NoraebangController {

    public final NoraebangService noraebangService;
    public final SongService songService;
    public final ArtistService artistService;
    public final S3Service s3Service;


    /*
    아티스트 정보 가져오기
    노래방 생성 화면으로 갔을 때 처음엔 아티스트 정보들을 보내줘야 아티스트를 선택할 수 있기 때문에 아티스트를 담아서 보냄
     */
    @GetMapping("/info")
    public Response<List<ArtistDto>> getArtist() {
        return Response.of("OK", "모든 아티스트 가져오기", artistService.getAllArtist());
    }

    /*
    곡 목록 가져오기
    아티스트 선택 후 해당 아티스트의 곡들을 받아야함
     */
    @GetMapping("/{artist_pk}/songs")
    public Response<List<SongDto>> InfoSongs(@PathVariable("artist_pk") Long artistPk) {
        return Response.of("OK", "아티스트의 모든 노래 가져오기", songService.getSongByArtist(artistPk));
    }

    /*
    모든 노래방 게시글 가져오기
     */
    @GetMapping("")
    public Response<List<NoraebangDto>> getNoraebangs() {
        return Response.of("OK", "모든 노래방 게시글 가져오기", noraebangService.getNoraebang());
    }

    /*
    노래방 게시글 디테일 정보 가져오기
     */
    @GetMapping("/{noraebang_pk}")
    public Response<NoraebangDto> getNoraebangDetail(@PathVariable("noraebang_pk") Long noraebangPk) {
        return Response.of("Ok", "노래방 게시글 디테일 정보 가져오기", noraebangService.getNoraebangDetail(noraebangPk));
    }

    /*
     게시글 작성하기.
     formData 형식으로 file과 게시글 내용, songPk, userPk필요
     */
    @PostMapping("")
    public Response<?> createNoraebang(@RequestParam MultipartFile file,
                                       @RequestParam String content,
                                       @RequestParam("song_pk") Long songPk,
                                       @RequestParam("user_pk") Long userPk) throws IOException {
        noraebangService.createNoraebang(file, content, songPk, userPk);
        return Response.of("Ok", "노래방 게시글 작성", new ArrayList<>());
    }


    /*
    노래방 게시글 내용 수정하기.
     */
    @PutMapping("")
    public Response<?> updateNoraebang(@RequestParam String content,
                                       @RequestParam Long noraebangPk) {
        noraebangService.updateNoraebang(content, noraebangPk);
        return Response.of("Ok", "노래방 게시글 수정", new ArrayList<>());
    }

    /*
    노래방 게시글 삭제하기
     */
    @DeleteMapping("/{noraebang_pk}")
    public Response<?> deleteNoraebang(@PathVariable("noraebang_pk") Long noraebangPk) {
        noraebangService.deleteNoraebang(noraebangPk);
        return Response.of("Ok", "댓글 삭제", new ArrayList<>());
    }


    /*
    노래방 게시글 댓글달기,
    게시글 pk, 유저 pk, 댓글 내용 필요.
     */
    @PostMapping("/{noraebang_pk}/review")
    public Response<?> createNoraebangReview(@PathVariable("noraebang_pk") Long noraebangPk, @RequestBody NoraebangReviewDto noraebangReviewDto) {
        noraebangService.createNoraebangReview(noraebangPk, noraebangReviewDto);
        return Response.of("Ok", "댓글 작성", null);
    }

    /*
    게시글 댓글 모두 조회
     */
    @GetMapping("/{noraebang_pk}/review")
    public Response<List<NoraebangReviewDto>> getNoraebangReviews(@PathVariable("noraebang_pk") Long noraebangPk) {
        return Response.of("Ok", "댓글 조회", noraebangService.getNoraebangReviews(noraebangPk));
    }

    /*
    게시글 댓글 삭제.
    작성자만 삭제할 수 있음.
     */
    @DeleteMapping("/{review_pk}/{user_pk}/review")
    public Response<?> deleteNoraebangReview(@PathVariable("noraebang_pk") Long reviewPk,
                                             @PathVariable("user_pk") Long userPk) {
        return Response.of("OK", "댓글 삭제", noraebangService.deleteNoraebangReview(reviewPk, userPk));
    }

    /*
    게시글 좋아요 기능.
     */
    @PostMapping("/{noraebang_pk}/{user_pk}/like")
    public Response<?> toggleNoraebangLike(@PathVariable("noraebang_pk") Long noraebangPk,
                                           @PathVariable("user_pk") Long userPk) {
        noraebangService.toggleNoraebangLike(noraebangPk, userPk);
        return Response.of("Ok", "좋아요 성공 및 삭제", null);
    }
}