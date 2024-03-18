package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.CoverDto;
import ssafy.navi.dto.NoraebangDto;
import ssafy.navi.entity.Cover;
import ssafy.navi.entity.Noraebang;
import ssafy.navi.repository.CoverRepository;
import ssafy.navi.repository.NoraebangRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final NoraebangRepository noraebangRepository;
    private final CoverRepository coverRepository;
    //유저 레포지토리 완성되면 추가하기


    /*
    통합검색 하기
    커버 제목 검색 3개, 노래방 제목 검색 3개 리스트 형태로 가져옴
    +커버 원곡자 검색 3개, 노래방 원곡자 검색 3개, 유저 검색 3개
    Map에 cover,noraebang 리스트들을 담아서 반환함
     */
    public Map<String,Object> getSearchAll(String keyword) throws Exception{
        Map<String,Object> searchAll=new HashMap<>();
        List<CoverDto> coverDtosTitle=coverRepository.findTop3ByTitleContainingOrderByCreatedAtDesc(keyword)
                .stream()
                .map(CoverDto::convertToDto)
                .collect(Collectors.toList());
        List<CoverDto> coverDtosArtist=coverRepository.findTop3ByArtistNameContainingOrderByCreatedAtDesc(keyword)
                .stream()
                .map(CoverDto::convertToDto)
                .collect(Collectors.toList());
        List<NoraebangDto> noraebangDtosTitle=noraebangRepository.findTop3BySongTitleContainingOrderByCreatedAtDesc(keyword)
                .stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
        List<NoraebangDto> noraeBangDtosArtist=noraebangRepository.findTop3ByArtistNameContainingOrderByCreatedAtDesc(keyword)
                .stream()
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
        List<NoraebangDto> noraebangDtos=noraebangRepository.findBySongTitleContainingOrderByCreatedAtDesc(keyword)
                .stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
        return noraebangDtos;
    }

    /*
    노래방 원곡자 검색 더보기 전체 리스트 가져옴
    쿼리스트링의 keyword로 검색하여 가져옴
     */
    public List<NoraebangDto> getSearchMoreNoraebangArtist (String keyword){
        List<NoraebangDto> noraebangDtos=noraebangRepository.findByArtistNameContainingOrderByCreatedAtDesc(keyword)
                .stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
        return noraebangDtos;
    }

    /*
    커버 제목 검색더보기 전체 리스트 가져옴
    쿼리스트링의 keyword로 검색하여 가져오게 됨
     */
    public List<CoverDto> getSearchMoreCoverTitle(String keyword) throws Exception{
        List<CoverDto> coverDtos=coverRepository.findByTitleContainingOrderByCreatedAtDesc(keyword)
                .stream()
                .map(CoverDto::convertToDtoList)
                .collect(Collectors.toList());
        return coverDtos;
    }
}
