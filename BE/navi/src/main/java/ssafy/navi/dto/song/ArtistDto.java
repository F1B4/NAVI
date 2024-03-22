package ssafy.navi.dto.song;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.song.Artist;

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
        artistDto.setId(artist.getId());
        artistDto.setPartCount(artist.getPartCount());
        artistDto.setName(artist.getName());
        return artistDto;
    }
}