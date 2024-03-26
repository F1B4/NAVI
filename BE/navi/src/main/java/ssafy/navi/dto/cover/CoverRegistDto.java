package ssafy.navi.dto.cover;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverRegistDto {
    private Long songPk;
    private List<UserPartDto> userPartDtos;
}
