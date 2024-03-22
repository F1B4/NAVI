package ssafy.navi.dto.song;

import lombok.*;
import ssafy.navi.entity.song.Part;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PartDto {

    private Long id;
    private String image;
    private String name;

    // 엔티티 Dto로 변환
    public static PartDto convertToDto(Part part) {
        PartDto partDto = new PartDto();

        // set
        partDto.setId(part.getId());
        partDto.setImage(part.getImage());
        partDto.setName(part.getName());


        return partDto;
    }
}
