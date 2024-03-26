package ssafy.navi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class ArtistService {

    private final ArtistRepository artistRepository;

    /*
    모든 원곡자 조회
     */
    public List<ArtistDto> getAllArtist() {
        List<Artist> artists = artistRepository.findAll();

        return artists.stream()
                .map(ArtistDto::convertToDto)
                .collect(Collectors.toList());
    }

}
