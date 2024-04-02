package ssafy.navi.dto.user;

import lombok.*;
import ssafy.navi.entity.user.Follow;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowingDto {

    private Long id;
    private UserDto user;

    // 엔티티 Dto로 변환
    public static FollowingDto convertToDto(Follow follow) {
        FollowingDto followingDto = new FollowingDto();

        // set
        followingDto.setId(follow.getId());
        followingDto.setUser(UserDto.convertToDto(follow.getToUser()));

        return followingDto;
    }
}
