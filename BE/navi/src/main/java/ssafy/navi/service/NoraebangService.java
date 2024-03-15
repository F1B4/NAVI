package ssafy.navi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.NoraebangDto;
import ssafy.navi.dto.SongDto;
import ssafy.navi.entity.Artist;
import ssafy.navi.entity.Noraebang;
import ssafy.navi.repository.NoraebangRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoraebangService {

    private final NoraebangRepository noraebangRepository;

    @Autowired
    public NoraebangService(NoraebangRepository noraebangRepository) {
        this.noraebangRepository = noraebangRepository;
    }

    public List<NoraebangDto> getAllNoraebang() {
        List<Noraebang> all = noraebangRepository.findAll();

        return all.stream().map(NoraebangDto::convertToDtoNoraebangs).toList();
    }

    public NoraebangDto getNoraebang(Long pk) {
        Noraebang noraebang = noraebangRepository.findById(pk)
                .orElseThrow(() -> new EntityNotFoundException("Norabang not found with id: " + pk));

        return NoraebangDto.convertToDtoDetail(noraebang);


    }
}
