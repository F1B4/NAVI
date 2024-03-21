package ssafy.navi.entity.song;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.cover.Cover;
import ssafy.navi.entity.cover.Matching;
import ssafy.navi.entity.noraebang.Noraebang;
import ssafy.navi.entity.user.Voice;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_pk")
    private Long id;

    @Column(name = "title")
    private String title;

    // 음원 mr S3 URL
    @Column(name = "mr")
    private String mr;

    // 음원 표지 S3 URL
    @Column(name = "image")
    private String image;

    //==외래키==//

    // 원곡자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_pk")
    private Artist artist;

    // 노래방 게시글
    @OneToMany(mappedBy = "song")
    private List<Noraebang> noraebangs;

    // Ai 커버 게시글
    @OneToMany(mappedBy = "song")
    private List<Cover> covers;

    // 매칭
    @OneToMany(mappedBy = "song")
    private List<Matching> matchings;

    // 가사
    @OneToMany(mappedBy = "song")
    private List<Lyric> lyrics;

    // 파트
    @OneToMany(mappedBy = "song")
    private List<Part> parts;

    // 목소리
    @OneToMany(mappedBy = "song")
    private List<Voice> voices;
}
