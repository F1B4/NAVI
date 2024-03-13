package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.navi.repository.NoraebangRepository;

@Service
@Slf4j
public class NoraebangService {

    private final NoraebangRepository noraebangRepository;

    @Autowired
    public NoraebangService(NoraebangRepository noraebangRepository) {
        this.noraebangRepository = noraebangRepository;
    }
}
