package ssafy.navi.entity.noraebang;

import jakarta.persistence.*;
import lombok.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.user.User;
import ssafy.navi.entity.song.Song;
import ssafy.navi.entity.util.BaseTimeEntity;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) //builder패턴을 사용하기 위해 추가
public class Noraebang extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noraebang_pk")
    private Long id;

    // 게시판 내용
    @Column(name = "content")
    private String content;

    // mr존재한 녹음파일 S3 URL
    @Column(name = "record")
    private String record;

    // 조회수
    @Column(name = "hit")
    private Integer hit = 0;

    // 좋아요 수
    @Column(name = "like_count")
    private Integer likeCount = 0;

    //==외래키==//

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    // 노래
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_pk")
    private Song song;

    // 노래방 좋아요
    @OneToMany(mappedBy = "noraebang", cascade = CascadeType.ALL)
    private List<NoraebangLike> noraebangLikes;

    // 노래방 댓글
    @OneToMany(mappedBy = "noraebang", cascade = CascadeType.ALL)
    private List<NoraebangReview> noraebangReviews;

}
