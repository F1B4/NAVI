package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.navi.repository.CoverRepository;
import ssafy.navi.repository.NoraebangReviewRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoraebangReviewService {

    private final NoraebangReviewRepository noraebangReviewRepository;
}
