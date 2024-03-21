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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/noraebangs")
public class NoraebangController {

    public final NoraebangService noraebangService;
    public final SongService songService;
    public final ArtistService artistService;
    public final S3Service s3Service;

    @GetMapping("/info/artists")
    public Response<List<ArtistDto>> InfoArtist() {
        return Response.of("Ok", "ArtistName", artistService.getAllArtist());
    }

    @GetMapping("/info/songs")
    public Response<List<SongDto>> InfoSongs(@RequestParam Long pk) {
        return Response.of("Ok", "SongsTitle", artistService.getAllArtistSong(pk));
    }

    @GetMapping("")
    public Response<List<NoraebangDto>> InfoNoraebang() {
        return Response.of("Ok", "get Noraebang articles", noraebangService.getAllNoraebang());
    }

    @GetMapping("/")
    public Response<NoraebangDto> InfoNoraebangDetail(@RequestParam Long noraebangPk) {
        return Response.of("Ok", "get Noraebang detail", noraebangService.getNoraebang(noraebangPk));
    }

    @PostMapping("")
    public Response<?> createNoraebang(@RequestParam MultipartFile file,
                                       @RequestParam String content,
                                       @RequestParam("song_pk") Long songPk,
                                       @RequestParam("user_pk") Long userPk) throws IOException {
        noraebangService.createNoraebang(file, content, songPk, userPk);
        return Response.of("Ok", "create Noraebang Article", new ArrayList<>());
    }

    @PutMapping("")
    public Response<?> updateNoraebang(@RequestParam String content,
                                       @RequestParam Long noraebangPk) {
        noraebangService.updateNoraebang(content, noraebangPk);
        return Response.of("Ok", "update Noraebang Article", new ArrayList<>());
    }

    @DeleteMapping("")
    public Response<?> deleteNoraebang(@RequestParam Long noraebangPk) {
        noraebangService.deleteNoraebang(noraebangPk);
        return Response.of("Ok", "delete Noraebang Article", new ArrayList<>());
    }

    @PostMapping("/review")
    public Response<?> createReviewNoraebang(@RequestParam Long noraebangPk,
                                             @RequestParam Long userPk,
                                             @RequestParam String content) {
        noraebangService.createReview(noraebangPk, userPk, content);
        return Response.of("Ok", "create Noraebang Review", new ArrayList<>());
    }

    @GetMapping("/review")
    public Response<List<NoraebangReviewDto>> getNoraebangReviews(@RequestParam Long noraebangPk) {
        return Response.of("Ok", "get Noraebang Reviews", noraebangService.getNoraebangReviews(noraebangPk));
    }

    @DeleteMapping("/review")
    public Response<?> deleteNoraebangReview(@RequestParam Long reviewPk,
                                             @RequestParam Long userPk) {
        String result = noraebangService.deleteReview(reviewPk, userPk);

        return Response.of(result, "delete Noraebang Review", new ArrayList<>());
    }

    @PostMapping("/like")
    public Response<?> toggleNoraebangLike(@RequestParam Long noraebangPk,
                                           @RequestParam Long userPk) {
        noraebangService.toggleNoraebangLike(noraebangPk, userPk);
        return Response.of("Ok", "toggle Noraebang Like", new ArrayList<>());
    }
}