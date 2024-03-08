package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.NoraebangLike;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoraebangLikeDto {

    private Long id;
    private UserDto user;
    private NoraebangDto noraebang;

    // 엔티티 Dto로 변환
    public static NoraebangLikeDto convertToDto(NoraebangLike noraebangLike) {
        NoraebangLikeDto noraebangLikeDto = new NoraebangLikeDto();

        // set
        noraebangLikeDto.setId(noraebangLike.getId());
        noraebangLikeDto.setUser(UserDto.convertToDto(noraebangLike.getUser()));
        noraebangLikeDto.setNoraebang(NoraebangDto.convertToDto(noraebangLike.getNoraebang()));

        return noraebangLikeDto;
    }
}
