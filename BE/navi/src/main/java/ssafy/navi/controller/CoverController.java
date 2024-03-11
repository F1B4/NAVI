package ssafy.navi.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.navi.entity.Cover;
import ssafy.navi.service.CoverService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/covers")
public class CoverController {

    @Autowired
    CoverService coverService;


    // get방식으로 /covers로 들어오는 주소에 매핑됨
    // 현재 coverPk를 기준으로 6개씩 조회하게 함
    @GetMapping("/")
    public ResponseEntity<List<Cover>> getCoverList(@RequestBody Long coverPk){
        return new ResponseEntity<>(coverService.getCoverList(coverPk), HttpStatus.OK);
    }

    @GetMapping("/{cover_pk}")
    public ResponseEntity<Cover> getCoverDetail(@PathVariable Long coverPk){
        Cover cover=coverService.getCoverDetail(coverPk);
        return ResponseEntity.status(200).body(cover);
    }

}
