package ssafy.navi.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.navi.dto.ArtistDto;
import ssafy.navi.dto.Response;
import ssafy.navi.entity.Song;
import ssafy.navi.service.ArtistService;
import ssafy.navi.service.NoraebangService;
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

    @GetMapping("/info/artist")
    public Response<List<ArtistDto>> InfoArtist() {
        return Response.of("Ok", "ArtistName", artistService.getAllArtist());
    }
}
