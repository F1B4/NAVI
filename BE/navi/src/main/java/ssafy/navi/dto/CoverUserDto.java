package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.CoverUser;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverUserDto {

    private Long id;
//    private UserDto user;
//    private CoverDto cover;

    // 엔티티 Dto로 변환
    public static CoverUserDto convertToDto(CoverUser coverUser) {
        CoverUserDto coverUserDto = new CoverUserDto();

        // set
        coverUserDto.setId(coverUser.getId());
//        coverUserDto.setUser(UserDto.convertToDto(coverUser.getUser()));
//        coverUserDto.setCover(CoverDto.convertToDto(coverUser.getCover()));

        return coverUserDto;
    }
}
