package ssafy.navi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.navi.entity.Cover;
import ssafy.navi.repository.CoverRepository;

import java.util.List;

@Service
@Slf4j
public class CoverService {
    @Autowired
    CoverRepository coverRepository;

    //
    public List<Cover> getCoverList(Long coverPk){
        return coverRepository.findbyId(coverPk);
    }
    public Cover getCoverDetail(Long coverPk){
        Cover cover=coverRepository.findbyId(coverPk);
        if(cover != null){
            return null;
        }
        return cover;
    }
}
