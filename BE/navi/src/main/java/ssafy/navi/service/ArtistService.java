package ssafy.navi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.ArtistDto;
import ssafy.navi.dto.Response;
import ssafy.navi.entity.Artist;
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

        return all.stream().map(ArtistDto::convertToDto).collect(Collectors.toList());
    }
}
