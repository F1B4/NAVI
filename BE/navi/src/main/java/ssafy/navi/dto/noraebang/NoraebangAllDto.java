package ssafy.navi.dto.noraebang;

import lombok.*;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.entity.noraebang.Noraebang;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoraebangAllDto {

    private Long id;
    private String record;
    private Integer hit;
    private Integer likeCount;
    private SongDto songDto;
    private UserDto userDto;

    public static NoraebangAllDto convertToDto(Noraebang noraebang) {
        NoraebangAllDto noraebangAllDto = new NoraebangAllDto();

        noraebangAllDto.setId(noraebang.getId());
        noraebangAllDto.setRecord(noraebang.getRecord());
        noraebangAllDto.setHit(noraebang.getHit());
        noraebangAllDto.setLikeCount(noraebang.getLikeCount());
        noraebangAllDto.setSongDto(SongDto.convertToDto(noraebang.getSong()));
        noraebangAllDto.setUserDto(UserDto.convertToDto(noraebang.getUser()));

        return noraebangAllDto;
    }

}
