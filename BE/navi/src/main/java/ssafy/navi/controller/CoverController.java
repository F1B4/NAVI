package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.navi.dto.CoverDto;
import ssafy.navi.dto.CoverLikeDto;
import ssafy.navi.service.CoverService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/covers")
public class CoverController {

    private final CoverService coverService;

    @GetMapping("")
    public String test(){
        return "Hello World";
    }

    /*
    커버 게시판 디테일 보기, pathvariable로 온 co-er_pk를 통해 조회해서 CoverDto에 정보를 담아와서 보내줌
     */
    @GetMapping("/{cover_pk}")
    public ResponseEntity<CoverDto> getCoverDetail(@PathVariable("cover_pk") Long coverPk) throws Exception {
        return new ResponseEntity<>(coverService.getCoverDetail(coverPk),HttpStatus.OK);
    }

//    @PostMapping("/{cover_pk}/like")
//    public ResponseEntity<CoverLikeDto> coverLike(@PathVariable("cover_pk") Long coverPk) throws Exception {
//        Long userPk=1L;
//        return new ResponseEntity<>(coverService.toggleCoverLike(coverPk,userPk),HttpStatus.OK);
//    }


}
