package ssafy.navi.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ssafy.navi.entity.cover.Cover;
import ssafy.navi.repository.CoverRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@SpringBootTest
class CoverServiceTest {

    @Autowired // 필드 주입
    private CoverRepository coverRepository;

    @Test
    void getHotCover() {

        // 불러올 날짜 가져오기
        Cover cover = coverRepository.findById(2L).get();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String str = "2024-03-25 12:51:35";
        LocalDateTime date = LocalDateTime.parse(str, formatter);

//      Assertions.assertThat(cover.getCreatedAt().plusWeeks(1).isEqual(date)).isTrue();
        Assertions.assertThat(cover.getCreatedAt().plusWeeks(1)).isEqualTo(date);
    }
}