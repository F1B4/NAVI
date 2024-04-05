package ssafy.navi.dto.noraebang;

import lombok.*;
import ssafy.navi.entity.noraebang.NoraebangLike;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoraebangLikeDto {

    private Long id;
    private NoraebangDto noraebangDto;
    // 엔티티 Dto로 변환
    public static NoraebangLikeDto convertToDto(NoraebangLike noraebangLike) {
        NoraebangLikeDto noraebangLikeDto = new NoraebangLikeDto();

        // set
        noraebangLikeDto.setId(noraebangLike.getId());
        noraebangLikeDto.setNoraebangDto(NoraebangDto.convertToDtoNoraebangs(noraebangLike.getNoraebang()));

        return noraebangLikeDto;
    }
}
