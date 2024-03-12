package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Follow;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowerDto {

    private Long id;
//    private UserDto toUserDto;
//    private UserDto fromUserDto;

    // 엔티티 Dto로 변환
    public static FollowerDto convertToDto(Follow follow) {
        FollowerDto followerDto = new FollowerDto();

        // set
        followerDto.setId(follow.getId());
//        followerDto.setToUserDto(UserDto.convertToDto(follow.getToUser()));
//        followerDto.setFromUserDto(UserDto.convertToDto(follow.getFromUser()));

        return followerDto;
    }
}
