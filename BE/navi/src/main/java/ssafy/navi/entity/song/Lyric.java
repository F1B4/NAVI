package ssafy.navi.entity.song;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lyric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lyric_pk")
    private Long id;

    // 가사 시작 부분
    @Column(name = "start_time")
    private LocalDateTime startTime;

    // 가사 끝나는 부분
    @Column(name = "end_time")
    private LocalDateTime endTime;

    // 가사 내용
    @Column(name = "content")
    private String content;

    // 가사 순서
    @Column(name = "sequence")
    private Integer sequence;

    //==외래키==//

    // 무슨 노래의 가사인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_pk")
    private Song song;

}
