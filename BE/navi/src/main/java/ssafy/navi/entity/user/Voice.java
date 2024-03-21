package ssafy.navi.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.song.Song;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_pk")
    private Long id;

    // 목소리 S3 URL 주소
    @Column(name = "path")
    private String path;

    //==외래키==//

    // 목소리 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    // 노래
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_pk")
    private Song song;
}
