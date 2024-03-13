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
//    private SongDto songDto;
//    private List<CoverUserDto> coverUserDtos;
//    private List<MatchingUserDto> matchingUserDtos;

    // 엔티티 Dto로 변환
    public static PartDto convertToDto(Part part) {
        PartDto partDto = new PartDto();

        // set
        partDto.setId(part.getId());
        partDto.setImage(part.getImage());
        partDto.setName(part.getName());
//        partDto.setSongDto(SongDto.convertToDto(part.getSong()));
//        partDto.setCoverUserDtos(part.getCoverUsers()
//                .stream().map(CoverUserDto::convertToDto)
//                .collect(Collectors.toList()));
//        partDto.setMatchingUserDtos(part.getMatchingUsers()
//                .stream().map(MatchingUserDto::convertToDto)
//                .collect(Collectors.toList()));


        return partDto;
    }
}
