package ssafy.navi.entity.noraebang;

import jakarta.persistence.*;
import lombok.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.user.User;
import ssafy.navi.entity.util.BaseTimeEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoraebangReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noraebang_review_pk")
    private Long id;

    @Column(name = "content")
    private String content;

    //==외래키==//

    // 댓글 단 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    // 댓글 단 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noraebang_pk")
    private Noraebang noraebang;

}
