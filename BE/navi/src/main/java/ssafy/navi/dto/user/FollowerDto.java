package ssafy.navi.dto.user;

import lombok.*;
import ssafy.navi.entity.user.Follow;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowerDto {

    private Long id;
    private UserDto user;

    // 엔티티 Dto로 변환
    public static FollowerDto convertToDto(Follow follow) {
        FollowerDto followerDto = new FollowerDto();

        // set
        followerDto.setId(follow.getId());
        followerDto.setUser(UserDto.convertToDto(follow.getFromUser()));

        return followerDto;
    }
}
