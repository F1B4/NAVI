package ssafy.navi.dto.cover;

import lombok.*;
import ssafy.navi.dto.song.PartDto;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.entity.cover.MatchingUser;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchingUserDto {

    private Long id;
    private UserDto userDto;
    private PartDto partDto;

    // 엔티티 Dto로 변환
    public static MatchingUserDto convertToDto(MatchingUser matchingUser) {
        MatchingUserDto matchingUserDto = new MatchingUserDto();

        // set
        matchingUserDto.setId(matchingUser.getId());
        matchingUserDto.setUserDto(UserDto.convertToDto(matchingUser.getUser()));
        matchingUserDto.setPartDto(PartDto.convertToDto(matchingUser.getPart()));

        return matchingUserDto;
    }
}
