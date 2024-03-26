package ssafy.navi.entity.song;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.cover.CoverUser;
import ssafy.navi.entity.cover.MatchingUser;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_pk")
    private Long id;

    // 멤버 프로필 사진 S3 URL
    @Column(name = "image")
    private String image;

    // 멤버 이름
    @Column(name = "name")
    private String name;

    //==외래키==//

    // 노래
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_pk")
    private Song song;

    // 게시글 사용자 중계 테이블
    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    private List<CoverUser> coverUsers;

    // 매칭 중계 테이블
    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    private List<MatchingUser> matchingUsers;
}
