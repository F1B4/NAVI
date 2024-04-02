package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.cover.CoverDto;
import ssafy.navi.dto.noraebang.*;
import ssafy.navi.dto.song.ArtistDto;
import ssafy.navi.dto.song.LyricDto;
import ssafy.navi.dto.user.CustomOAuth2User;
import ssafy.navi.dto.util.Response;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.UserRepository;
import ssafy.navi.service.*;

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
    public final NotificationService notificationService;
    public final UserService userService;
    private final UserRepository userRepository;



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

    @GetMapping("/{song_pk}/lyrics")
    public Response<List<LyricDto>> getLyrics(@PathVariable("song_pk") Long songPk) {
        return Response.of("OK", "가사 가져오기", noraebangService.getLyrics(songPk));
    }


    /*
    모든 노래방 게시글 가져오기
     */
    @GetMapping("")
    public Response<List<NoraebangAllDto>> getNoraebangs() {
        return Response.of("OK", "모든 노래방 게시글 가져오기", noraebangService.getNoraebang());
    }

    /*
    노래방 게시글 디테일 정보 가져오기
     */
    @GetMapping("/detail/{noraebang_pk}/{user_pk}")
    public Response<NoraebangDetailDto> getNoraebangDetail(@PathVariable("noraebang_pk") Long noraebangPk,
                                                           @PathVariable("user_pk") Long userPk) throws Exception {
        return Response.of("OK", "노래방 게시글 디테일 정보 가져오기", noraebangService.getNoraebangDetail(noraebangPk,userPk));
    }

    /*
     게시글 작성하기.
     formData 형식으로 file과 게시글 내용, songPk 필요
     */
    @PostMapping("/create")
    public Response<?> createNoraebang(@RequestParam MultipartFile file,
                                       @RequestParam String content,
                                       @RequestParam("song_pk") String songPk) throws Exception {
        noraebangService.createNoraebang(file, content, Long.valueOf(songPk));
        return Response.of("Ok", "노래방 게시글 작성", null);
    }

    @PostMapping("/complete")
    public void recordNoraebang() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());
        notificationService.sendNotificationToUser(user.getId(), "노래방 생성 완료");
    }

    /*
    노래방 게시글 내용 수정하기.
     */
    @PutMapping("/update")
    public Response<?> updateNoraebang(@RequestParam String content,
                                       @RequestParam("noraebnag_pk") Long noraebangPk) {
        noraebangService.updateNoraebang(content, noraebangPk);
        return Response.of("OK", "노래방 게시글 수정", null);
    }

    /*
    노래방 게시글 삭제하기
     */
    @DeleteMapping("/{noraebang_pk}")
    public Response<?> deleteNoraebang(@PathVariable("noraebang_pk") Long noraebangPk) throws Exception {
        noraebangService.deleteNoraebang(noraebangPk);
        return Response.of("OK", "댓글 삭제", null);
    }


    /*
    노래방 게시글 댓글달기,
    게시글 pk, 유저 pk, 댓글 내용 필요.
     */
    @PostMapping("/{noraebang_pk}/review")
    public Response<?> createNoraebangReview(@PathVariable("noraebang_pk") Long noraebangPk, @RequestBody String content) throws Exception {
        noraebangService.createNoraebangReview(noraebangPk, content);
        return Response.of("OK", "댓글 작성", null);
    }

    /*
    게시글 댓글 삭제.
    작성자만 삭제할 수 있음.
     */
    @DeleteMapping("/review/{review_pk}")
    public Response<?> deleteNoraebangReview(@PathVariable("review_pk") Long reviewPk) throws Exception {
        return Response.of("OK", "댓글 삭제", noraebangService.deleteNoraebangReview(reviewPk));
    }

    /*
    게시글 좋아요 기능.
     */
    @PostMapping("/{noraebang_pk}/like")
    public Response<?> toggleNoraebangLike(@PathVariable("noraebang_pk") Long noraebangPk) {
        noraebangService.toggleNoraebangLike(noraebangPk);
        return Response.of("Ok", "좋아요 성공 및 삭제", null);

    }

    /*
커버 게시판 목록 가져오기
조회수 순
 */
    @GetMapping("/byView")
    public Response<List<NoraebangAllDto>> getNoraebangByView() throws Exception{
        return Response.of("OK","게시글 목록 조회순으로 가져오기",noraebangService.getNoraebangByView());
    }

    /*
    커버 게시판 정렬하기
    좋아요 순
     */
    @GetMapping("/byLike")
    public Response<List<NoraebangAllDto>> getNoraebangByLike() throws Exception{
        return Response.of("OK","게시글 목록 좋아요 순으로 가져오기",noraebangService.getNoraebangByLike());
    }
}