package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.navi.service.CoverService;
import ssafy.navi.service.NoraebangService;
import ssafy.navi.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
        private final NoraebangService noraebangService;
        private final CoverService coverService;
        // 유저 서비스를 통해 유저 조회 해야함



}
