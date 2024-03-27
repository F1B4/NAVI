package ssafy.navi.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ssafy.navi.entity.cover.CoverLike;
import ssafy.navi.entity.cover.CoverReview;
import ssafy.navi.entity.cover.CoverUser;
import ssafy.navi.entity.cover.MatchingUser;
import ssafy.navi.entity.noraebang.Noraebang;
import ssafy.navi.entity.noraebang.NoraebangLike;
import ssafy.navi.entity.noraebang.NoraebangReview;

import java.util.List;

@Entity
@Getter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_pk")
    private Long id;

    // 소셜 로그인 id
    @Column(name="username")
    private String username;

    // 소셜 로그인 설정된 이름
    @Column(name="nickname")
    private String nickname;

    // 소셜로그인 email
    @Column(name="email")
    private String email;

    // 프로필 사진 S3 URL
    @Column(name="image")
    private String image;

    // 유저 권한
    @Column(name="role")
    private Role role;

    // 팔로잉 수
    @Column(name = "following_count")
    private Integer followingCount;

    // 팔로워 수
    @Column(name = "follower_count")
    private Integer followerCount;

    // 모델 경로
    @Column(name ="model")
    private String model;

    // 인덱스 경로
    @Column(name="model_index")
    private String modelIndex;

    // 현재 매칭중인 수
    @Column(name="matching_count")
    private String matchingCount;

    // 노래방
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Noraebang> noraebangs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<NoraebangLike> noraebangLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<NoraebangReview> noraebangReviews;

    // AI 커버
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CoverUser> coverUsers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CoverLike> coverLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CoverReview> coverReviews;

    // 팔로워
    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL)
    private List<Follow> followers;

    // 팔로잉
    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL)
    private List<Follow> followings;

    // 알람
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alarm> alarms;

    // 매칭 중계 테이블
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MatchingUser> matchingUsers;

    // 목소리 목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Voice> voices;

    //==빌더 패턴==//
    @Builder
    public User(String username, String nickname, String email, String image, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.image = image;
        this.role = role;
        followingCount = 0;
        followerCount = 0;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateImage(String fileName) { this.image = fileName; }

    public void updateNickname(String nickname) { this.nickname = nickname; }
}
