package ssafy.navi.dto.cover;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserPartDto {
    private Long userPk;
    private Long partPk;
}
