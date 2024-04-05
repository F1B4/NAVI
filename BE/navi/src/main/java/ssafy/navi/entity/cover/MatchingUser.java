package ssafy.navi.entity.cover;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ssafy.navi.entity.song.Part;
import ssafy.navi.entity.user.User;

@Entity
@Getter @Setter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="matching_user_pk")
    private Long id;

    //==외래키==//

    // 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    // 매칭
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_pk")
    private Matching matching;

    // 파트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_pk")
    private Part part;

    @Builder
    public MatchingUser(User user,Matching matching,Part part){
        this.user=user;
        this.matching=matching;
        this.part=part;
    }
}
