package ssafy.navi.dto.cover;

import lombok.*;
import ssafy.navi.dto.song.PartDto;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.entity.cover.CoverUser;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverUserDto {

    private Long id;
    private UserDto userDto;
    private PartDto partDto;
    private CoverDto coverDto;

    // 엔티티 Dto로 변환
    public static CoverUserDto convertToDto(CoverUser coverUser) {
        CoverUserDto coverUserDto = new CoverUserDto();

        // set
        coverUserDto.setId(coverUser.getId());
        coverUserDto.setUserDto(UserDto.convertToDto(coverUser.getUser()));
        coverUserDto.setPartDto(PartDto.convertToDto(coverUser.getPart()));

        return coverUserDto;
    }
}
