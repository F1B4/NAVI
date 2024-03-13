package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.navi.service.NoraebangService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/noraebangs")
public class NoraebangController {

    public final NoraebangService noraebangService;

    @GetMapping("/test")
    public String test() {
        return "ojokoko";
    }
}
