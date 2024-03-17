package ssafy.navi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoraebangLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="noraebang_like_pk")
    private Long id;
    
    //==외래키==//
    
    // 좋아요 누른 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;
    
    // 좋아요 누른 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noraebang_pk")
    private Noraebang noraebang;

}
