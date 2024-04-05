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
public class CoverUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cover_user_pk")
    private Long id;

    //==외래키==//

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_pk")
    private Cover cover;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_pk")
    private Part part;

    @Builder
    public CoverUser (User user,Cover cover, Part part){
        this.user=user;
        this.cover=cover;
        this.part=part;
    }

}
