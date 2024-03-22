package ssafy.navi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.song.ArtistDto;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.entity.song.Artist;
import ssafy.navi.repository.ArtistRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArtistService {

    private final ArtistRepository artistRepository;
    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<ArtistDto> getAllArtist() {
        List<Artist> all = artistRepository.findAll();
        for (Artist name : all) {
            System.out.println(name.getName());
        }

        return all.stream().map(ArtistDto::convertToDto).collect(Collectors.toList());
    }

    public List<SongDto> getAllArtistSong(Long artistPk) {
        Artist artist = artistRepository.findById(artistPk)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistPk));

        return artist.getSongs().stream()
                .map(SongDto::convertToDto).collect(Collectors.toList());
    }
}
