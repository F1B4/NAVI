package ssafy.navi.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.ArtistDto;
import ssafy.navi.dto.NoraebangDto;
import ssafy.navi.dto.Response;
import ssafy.navi.dto.SongDto;
import ssafy.navi.entity.Song;
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

    @GetMapping("/info/songs/{artist_pk}")
    public Response<List<SongDto>> InfoSongs(@PathVariable("artist_pk") Long pk) {
        return Response.of("Ok", "SongsTitle", artistService.getAllArtistSong(pk));
    }

    @GetMapping("")
    public Response<List<NoraebangDto>> InfoNoraebang() {
        return Response.of("Ok", "get Noraebang articles", noraebangService.getAllNoraebang());
    }

    @GetMapping("/{noraebang_pk}")
    public Response<NoraebangDto> InfoNoraebangDetail(@PathVariable("noraebang_pk") Long pk) {
        return Response.of("Ok", "get Noraebang detail", noraebangService.getNoraebang(pk));
    }
//asdf
//    @PostMapping("/create")
//    public String createProduct(@RequestParam("files") MultipartFile[] files) throws IOException {
//        System.out.println("hereee-=-=--=-==--=-=-");
//        for (MultipartFile file : files) {
//            String imageUrl = s3Service.saveFile(file); // S3에 파일 업로드
//        }
//        return "asdfasdfasdf";
//    }
}