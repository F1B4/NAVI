package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Part;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PartDto {

    private Long id;
    private String image;
    private String name;
    private SongDto song;
    private List<MatchingDto> matchingDtos;

    // 엔티티 Dto로 변환
    public static PartDto convertToDto(Part part) {
        PartDto partDto = new PartDto();

        // set
        partDto.setId(part.getId());
        partDto.setImage(part.getImage());
        partDto.setName(part.getName());
        partDto.setSong(SongDto.convertToDto(part.getSong()));
        partDto.setMatchingDtos(part.getMatchings()
                .stream().map(MatchingDto::convertToDto)
                .collect(Collectors.toList()));

        return partDto;
    }
}
