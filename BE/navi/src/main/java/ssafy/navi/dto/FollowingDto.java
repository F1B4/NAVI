package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Follow;
import ssafy.navi.entity.User;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowingDto {

    private Long id;
//    private UserDto toUser;
//    private UserDto fromUser;

    // 엔티티 Dto로 변환
    public static FollowingDto convertToDto(Follow follow) {
        FollowingDto followDto = new FollowingDto();

        // set
        followDto.setId(follow.getId());
//        followDto.setToUser(UserDto.convertToDto(follow.getToUser()));
//        followDto.setFromUser(UserDto.convertToDto(follow.getFromUser()));

        return followDto;
    }
}
