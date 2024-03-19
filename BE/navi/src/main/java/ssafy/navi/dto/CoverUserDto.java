package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.CoverUser;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverUserDto {

    private Long id;
    private UserDto userDto;
//    private CoverDto coverDto;
    private PartDto partDto;

    // 엔티티 Dto로 변환
    public static CoverUserDto convertToDto(CoverUser coverUser) {
        CoverUserDto coverUserDto = new CoverUserDto();

        // set
        coverUserDto.setId(coverUser.getId());
        coverUserDto.setUserDto(UserDto.convertToDto(coverUser.getUser()));
//        coverUserDto.setCoverDto(CoverDto.convertToDto(coverUser.getCover()));
        coverUserDto.setPartDto(PartDto.convertToDto(coverUser.getPart()));

        return coverUserDto;
    }
}
