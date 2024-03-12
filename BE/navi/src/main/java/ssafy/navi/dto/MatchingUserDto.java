package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.MatchingUser;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchingUserDto {

    private Long id;
//    private UserDto user;
//    private MatchingDto matching;

    // 엔티티 Dto로 변환
    public static MatchingUserDto convertToDto(MatchingUser matchingUser) {
        MatchingUserDto matchingUserDto = new MatchingUserDto();

        // set
        matchingUserDto.setId(matchingUser.getId());
//        matchingUserDto.setUser(UserDto.convertToDto(matchingUser.getUser()));
//        matchingUserDto.setMatching(MatchingDto.convertToDto(matchingUser.getMatching()));

        return matchingUserDto;
    }
}
