package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.*;
import ssafy.navi.entity.*;
import ssafy.navi.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoverService {

    private final CoverRepository coverRepository;
    private final CoverReviewRepository coverReviewRepository;
    private final CoverUserRepository coverUserRepository;
    private final UserRepository userRepository;
    private final CoverLikeRepository coverLikeRepository;

    /*

     */

    /*
    orElseThrow를 통해 값이 없을경우 예외처리를 함
    orElseThrow를 사용하는 이유 -> jpa는 optional타입으로 반환하기 때문.
    커버 정보, 커버 댓글, 원곡 정보, 각자 맡은 파트
    원곡 정보는 Cover에 포함되어 있기 때문에 따로 선언하지 않음.
     */
    public Map<String, Object> getCoverDetail(Long coverPk) throws Exception {
        Map<String,Object> coverDetail=new HashMap<>();
        CoverDto coverDto = coverRepository.findById(coverPk).map(cover -> {
            cover.setHit(cover.getHit() + 1); // 조회수 증가
            return coverRepository.save(cover); // 변경된 엔티티 저장
        })
                .map(CoverDto::convertToDto).orElseThrow(() -> new Exception("커버가 없어요"));

        List<CoverUserDto> coverUserDtos=coverUserRepository.findByCover_Id(coverPk)
                .stream()
                .map(CoverUserDto::convertToDto)
                .collect(Collectors.toList());

        coverDetail.put("cover",coverDto);
        coverDetail.put("CoverUser",coverUserDtos);
        return coverDetail;
    }

    /*
    유저와 게시글이 있는지 확인하고 있다면 해당 값을 댓글에 값을 세팅하고 db에 추가함
    게시글이 존재하는지 체크 -> 유저가 존재하는지 체크 -> 둘다 충족한다면 받아온 내용을 저장하고 저장되는 내용을 반환함
     */
    public CoverReviewDto createCoverReview(Long coverPk,CoverReviewDto coverReviewDto) throws Exception{
        Cover cover=coverRepository.findById(coverPk)
                .orElseThrow(()->new Exception("커버 게시글이 존재하지 않아요"));
        User user=userRepository.findById(Long.valueOf(1))
                .orElseThrow(()->new Exception("유저가 없어요"));

        CoverReview coverReview= new CoverReview(coverReviewDto.getContent(), cover,user);
        coverReview=coverReviewRepository.save(coverReview);
        return CoverReviewDto.convertToDto(coverReview);
    }

    /*
    댓글 삭제
     */
    public String deleteCoverReview(Long coverPk,Long coverReviewPk) throws Exception{
        Cover cover=coverRepository.findById(coverPk)
                .orElseThrow(()->new Exception("커버 게시글이 존재하지 않아요"));
        User user=userRepository.findById(Long.valueOf(1))
                .orElseThrow(()->new Exception("유저가 없어요"));
        CoverReview coverReview=coverReviewRepository.findById(coverReviewPk)
                .orElseThrow(()->new Exception("댓글이 없어요"));
        coverReviewRepository.delete(coverReview);
        String msg="댓글 삭제가 잘 되었네요~";
        return msg;
    }

    /*
    커버 좋아요
     */
    public CoverLikeDto coverLike(Long coverPk) throws Exception{
        Cover cover=coverRepository.findById(coverPk)
                .orElseThrow(()->new Exception("커버 게시글이 존재하지 않아요"));
        User user=userRepository.findById(Long.valueOf(1))
                .orElseThrow(()->new Exception("유저가 없어요"));
        Optional<CoverLike> exists = coverLikeRepository.findByCoverAndUser(cover, user);

        if (exists.isPresent()) {
            // 이미 좋아요를 눌렀다면, 좋아요 삭제
            coverLikeRepository.delete(exists.get());
            int likeCount = cover.getLikeCount() == null ? 0 : cover.getLikeCount() - 1;
            cover.setLikeCount(likeCount);
            coverRepository.save(cover);
            return null;
        }else {
            // 좋아요가 없다면, 새로운 좋아요 생성
            CoverLike like=new CoverLike(cover,user);
            coverLikeRepository.save(like);
            int likeCount = cover.getLikeCount() == null ? 1 : cover.getLikeCount() + 1;
            cover.setLikeCount(likeCount);
            coverRepository.save(cover);
            return CoverLikeDto.convertToDto(like);
        }
    }
}
