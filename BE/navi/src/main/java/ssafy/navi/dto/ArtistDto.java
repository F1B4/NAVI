package ssafy.navi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.Artist;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {

    private Long id;
    private String name;
    private Integer partCount;
//    private List<SongDto> songDtos;

    // 엔티티 Dto로 변환
    public static ArtistDto convertToDto(Artist artist) {
        ArtistDto artistDto = new ArtistDto();

        // set
        artistDto.setId(artist.getId());
        artistDto.setName(artist.getName());
        artistDto.setPartCount(artist.getPartCount());
//        artistDto.setSongDtos(artist.getSongs()
//                .stream().map(SongDto::convertToDto)
//                .collect(Collectors.toList()));

        return artistDto;
    }
}