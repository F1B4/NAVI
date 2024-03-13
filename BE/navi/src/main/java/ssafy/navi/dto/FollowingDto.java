package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Follow;
import ssafy.navi.entity.User;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowingDto {

    private Long id;
//    private UserDto toUserDto;
//    private UserDto fromUserDto;

    // 엔티티 Dto로 변환
    public static FollowingDto convertToDto(Follow follow) {
        FollowingDto followDto = new FollowingDto();

        // set
        followDto.setId(follow.getId());
//        followDto.setToUserDto(UserDto.convertToDto(follow.getToUser()));
//        followDto.setFromUserDto(UserDto.convertToDto(follow.getFromUser()));

        return followDto;
    }
}
