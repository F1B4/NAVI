package ssafy.navi.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_pk")
    private Long id;

    // 팔로우 받는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user")
    private User toUser;

    // 팔로우하는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private User fromUser;

    @Builder
    public Follow (User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

}
