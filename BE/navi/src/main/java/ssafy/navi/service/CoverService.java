package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.CoverDto;
import ssafy.navi.dto.CoverLikeDto;
import ssafy.navi.entity.Cover;
import ssafy.navi.entity.User;
import ssafy.navi.repository.CoverRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoverService {

    private final CoverRepository coverRepository;



    /*
    coverPk를 이용해서 조회하고 해당 정보를 converToDto 메소드를 이용하여 Dto에 정보를 담아 Dto를 반환함!
    orElseThrow를 통해 값이 없을경우 예외처리를 함
    Optional을 사용하는 이유 : JPA는 Optional 객체로 반환하기 때문
     */
    public CoverDto getCoverDetail(Long coverPk) throws Exception {
        return coverRepository.findById(coverPk)
                .map(CoverDto::convertToDto)
                .orElseThrow(()-> new Exception("Cover is null"));
    }


}
