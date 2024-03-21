package ssafy.navi.dto.user;

import lombok.*;
import ssafy.navi.entity.user.User;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDto {

    private String nickname;
    private String email;
    private String image;
    private Integer followingCount;
    private Integer followerCount;
    // 엔티티 Dto로 변환
    public static UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();

        // set
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());
        userDto.setImage(user.getImage());
        userDto.setFollowingCount(user.getFollowingCount());
        userDto.setFollowerCount(user.getFollowerCount());
        return userDto;
    }
    public static UserDto convertToDtoCoverDetail(User user){
        UserDto userDto = new UserDto();
        // set
        userDto.setNickname(user.getNickname());
        userDto.setImage(user.getImage());
        return userDto;
    }
}
