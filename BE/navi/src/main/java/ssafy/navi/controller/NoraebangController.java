package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ssafy.navi.dto.song.ArtistDto;
import ssafy.navi.dto.noraebang.NoraebangDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.service.ArtistService;
import ssafy.navi.service.NoraebangService;
import ssafy.navi.service.S3Service;
import ssafy.navi.service.SongService;

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