package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.cover.CoverDto;
import ssafy.navi.dto.noraebang.NoraebangDto;
import ssafy.navi.dto.util.TimeDto;
import ssafy.navi.entity.cover.Cover;
import ssafy.navi.entity.noraebang.Noraebang;
import ssafy.navi.repository.CoverRepository;
import ssafy.navi.repository.NoraebangRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {
    private final CoverRepository coverRepository;
    private final NoraebangRepository noraebangRepository;

    /*
    최신 컨텐츠 가져오기
     */
    public List<TimeDto> getNewContents(){
        List<TimeDto> covers=coverRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(CoverDto::convertToDtoList)
                .collect(Collectors.toList());
        List<TimeDto> noraebangs=noraebangRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
        List<TimeDto> newList = new ArrayList<>(covers);
        newList.addAll(noraebangs);
        // 리스트를 생성시간으로 내림차순 정렬
        newList.sort(Comparator.comparing(TimeDto::getCreatedAt).reversed());

        // 정렬된 리스트에서 최신 10개의 데이터만 반환
        return newList.stream().limit(10).collect(Collectors.toList());
    }

    /*
        Hot 노래방 게시글 조회하기
        오늘 날짜로 부터 1주일 이내의 게시글 중 조회수가 가장 높은 10개를 조회함
        LocalDate타입에서 LocalDateTime으로 변환하는 이유 : 1주일 전의 자정을 기준으로 조회하기 위해서 시간개념이 포함된 LocalDateTime으로 변환함
         */
    public List<NoraebangDto> getHotNoraebang() {
        //1주일 전 날짜를 알아냄
        LocalDate oneWeek = LocalDate.now().minus(Period.ofWeeks(1));
        //1주일 전 날짜의 자정으로 값 지정
        LocalDateTime oneWeekAgo = oneWeek.atStartOfDay();
        List<Noraebang> noraebangs = noraebangRepository.findTop10ByCreatedAtAfterOrderByHitDesc(oneWeekAgo);
        return noraebangs.stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
    }

    /*
    Hot 커버 게시글 조회하기
    오늘 날짜로 부터 1주일 이내의 게시글 중 조회수가 가장 높은 6개를 조회함
    LocalDate타입에서 LocalDateTime으로 변환하는 이유 : 1주일 전의 자정을 기준으로 조회하기 위해서 시간개념이 포함된 LocalDateTime으로 변환함
     */
    public List<CoverDto> getHotCover() {
        //1주일 전 날짜를 알아냄
        LocalDate oneWeek = LocalDate.now().minus(Period.ofWeeks(1));
        //1주일 전 날짜의 자정으로 값 지정
        LocalDateTime oneWeekAgo = oneWeek.atStartOfDay();
        List<Cover> covers = coverRepository.findTop6ByCreatedAtAfterOrderByWeeklyHitDesc(oneWeekAgo);
        return covers.stream()
                .map(CoverDto::convertToDtoList)
                .collect(Collectors.toList());
    }

    /*
    통합검색 하기
    커버 제목 검색 3개, 노래방 제목 검색 3개 리스트 형태로 가져옴
    +커버 원곡자 검색 3개, 노래방 원곡자 검색 3개, 유저 검색 3개
    Map에 cover,noraebang 리스트들을 담아서 반환함
     */
    public Map<String,Object> getSearchAll(String keyword) throws Exception{
        Map<String,Object> searchAll=new HashMap<>();
        List<CoverDto> coverDtosTitle=coverRepository.findTop3ByTitleContainingOrderByCreatedAtDesc(keyword).stream()
                .map(CoverDto::convertToDtoSearch)
                .collect(Collectors.toList());
        List<CoverDto> coverDtosArtist=coverRepository.findTop3ByArtistNameContainingOrderByCreatedAtDesc(keyword).stream()
                .map(CoverDto::convertToDtoSearch)
                .collect(Collectors.toList());
        List<NoraebangDto> noraebangDtosTitle=noraebangRepository.findTop3BySongTitleContainingOrderByCreatedAtDesc(keyword).stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
        List<NoraebangDto> noraeBangDtosArtist=noraebangRepository.findTop3ByArtistNameContainingOrderByCreatedAtDesc(keyword).stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());

        //유저 완성되면 추가하기
        searchAll.put("cover",coverDtosTitle);
        searchAll.put("coverArtist",coverDtosArtist);
        searchAll.put("noradebang",noraebangDtosTitle);
        searchAll.put("noraebangArtist",noraeBangDtosArtist);
        return searchAll;
    }

    /*
    노래방 제목 검색 더보기 전체 리스트 가져옴
    쿼리스트링의 keyword로 검색하여 가져옴
     */
    public List<NoraebangDto> getSearchMoreNoraebangTitle(String keyword) throws Exception{
        List<NoraebangDto> noraebangDtos=noraebangRepository.findBySongTitleContainingOrderByCreatedAtDesc(keyword).stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
        return noraebangDtos;
    }

    /*
    노래방 원곡자 검색 더보기 전체 리스트 가져옴
    쿼리스트링의 keyword로 검색하여 가져옴
     */
    public List<NoraebangDto> getSearchMoreNoraebangArtist (String keyword){
        List<NoraebangDto> noraebangDtos=noraebangRepository.findByArtistNameContainingOrderByCreatedAtDesc(keyword).stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
        return noraebangDtos;
    }

    /*
    커버 제목 검색 더보기 전체 리스트 가져옴
    쿼리스트링의 keyword로 검색하여 가져오게 됨
     */
    public List<CoverDto> getSearchMoreCoverTitle(String keyword) throws Exception{
        List<CoverDto> coverDtos=coverRepository.findByTitleContainingOrderByCreatedAtDesc(keyword).stream()
                .map(CoverDto::convertToDtoList)
                .collect(Collectors.toList());
        return coverDtos;
    }

    /*
    커버 원곡자 검색 더보기 전체 리스트 가져옴
    쿼리스트링의 keyword로 검색하여 가져오게 됨
     */
    public List<CoverDto> getSearchMoreCoverArtist(String keyword)throws  Exception{
        List<CoverDto> coverDtos=coverRepository.findByArtistNameContainingOrderByCreatedAtDesc(keyword).stream()
                .map(CoverDto::convertToDtoList)
                .collect(Collectors.toList());
        return coverDtos;
    }
}
