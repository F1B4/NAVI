package ssafy.navi.dto.user;

import lombok.*;
import ssafy.navi.entity.user.Role;
import ssafy.navi.entity.user.User;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String nickname;
    private String email;
    private String image;
    private Integer followingCount;
    private Integer followerCount;
    private String username;
    private Role role;
    // 엔티티 Dto로 변환
    public static UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();

        // set
        userDto.setId(user.getId());
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());
        userDto.setImage(user.getImage());
        userDto.setFollowingCount(user.getFollowingCount());
        userDto.setFollowerCount(user.getFollowerCount());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static UserDto convertToDtoCoverDetail(User user){
        UserDto userDto = new UserDto();
        // set
        userDto.setNickname(user.getNickname());
        userDto.setImage(user.getImage());
        return userDto;
    }
    public static UserDto convertToDtoMutualFollow(User user){
        UserDto userDto=new UserDto();
        //set
        userDto.setId(user.getId());
        userDto.setNickname(user.getNickname());
        userDto.setImage(user.getImage());
        return userDto;
    }

    //==빌더 패턴==//
    @Builder
    public UserDto(String username, Role role) {
        this.username = username;
        this.role = role;
    }
}
