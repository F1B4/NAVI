package ssafy.navi.entity.cover;

import jakarta.persistence.*;
import lombok.*;
import ssafy.navi.entity.song.Song;
import ssafy.navi.entity.util.BaseTimeEntity;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cover extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cover_pk")
    private Long id;

    // 게시판 제목
    @Column(name = "title")
    private String title;

    // 영상 S3 URL
    @Column(name = "video")
    private String video;

    // 썸네일 S3 URL
    @Column(name = "thumbnail")
    private String thumbnail;

    // 조회수
    @Column(name = "hit")
    private Integer hit;

    //주간 조회수
    @Column(name="weekly_hit")
    private Integer weeklyHit;

    // 좋아요 수
    @Column(name = "like_count")
    private Integer likeCount;


    //==외래키==//

    // 노래 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_pk")
    private Song song;

    // 커버 사용자 중계 테이블
    @OneToMany(mappedBy = "cover", cascade = CascadeType.ALL)
    private List<CoverUser> coverUsers;

    @OneToMany(mappedBy = "cover", cascade = CascadeType.ALL)
    private List<CoverLike> coverLikes;

    @OneToMany(mappedBy = "cover", cascade = CascadeType.ALL)
    private List<CoverReview> coverReviews;

    @Builder
    public Cover(String title,int hit,int likeCount, Song song){
        this.title=title;
        this.hit=hit;
        this.likeCount=likeCount;
        this.song=song;
    }
}
