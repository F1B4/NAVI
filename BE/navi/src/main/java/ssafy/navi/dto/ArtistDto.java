package ssafy.navi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.Artist;
import ssafy.navi.entity.Cover;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {

    private Long id;
    private Integer partCount;
    private String name;

    public static ArtistDto convertToDto(Artist artist) {
        ArtistDto artistDto = new ArtistDto();
        // set
        artistDto.setId(artistDto.getId());
        artistDto.setPartCount(artistDto.getPartCount());
        artistDto.setName(artistDto.getName());

        return artistDto;
    }
}