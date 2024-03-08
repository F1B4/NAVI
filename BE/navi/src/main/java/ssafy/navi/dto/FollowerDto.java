package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Follow;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowerDto {

    private Long id;
    private UserDto toUser;
    private UserDto fromUser;

    // 엔티티 Dto로 변환
    public static FollowerDto convertToDto(Follow follow) {
        FollowerDto followerDto = new FollowerDto();

        // set
        followerDto.setId(follow.getId());
        followerDto.setToUser(UserDto.convertToDto(follow.getToUser()));
        followerDto.setFromUser(UserDto.convertToDto(follow.getFromUser()));

        return followerDto;
    }
}
