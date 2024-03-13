package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.navi.dto.Response;
import ssafy.navi.entity.Song;
import ssafy.navi.service.NoraebangService;
import ssafy.navi.service.SongService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/noraebangs")
public class NoraebangController {

    public final NoraebangService noraebangService;
    public final SongService songService;

//    @GetMapping("/test")
//    public Response<Song> test() {
//        return Response.of("성공", "가수 이름",);
//    }
}
