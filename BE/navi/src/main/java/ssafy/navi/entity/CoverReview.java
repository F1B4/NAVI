package ssafy.navi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoverReview extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cover_review_pk")
    private Long id;

    @Column(name = "content")
    private String content;

    //==외래키==//

    // 댓글이 달린 게시물
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_pk")
    private Cover cover;

    // 댓글 단 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

}
