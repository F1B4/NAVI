package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Cover;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverDto implements TimeDto {

    private Long id;
    private String title;
    private String video;
    private String thumbnail;
    private Integer hit;
    private Integer likeCount;
    private SongDto songDto;
    private LocalDateTime createdAt;
//    private List<CoverUserDto> coverUserDtos;
//    private List<CoverLikeDto> coverLikeDtos;
    private List<CoverReviewDto> coverReviewDtos;
    // 엔티티 Dto로 변환
    public static CoverDto convertToDto(Cover cover) {
        CoverDto coverDto = new CoverDto();
        // set
        coverDto.setId(cover.getId());
        coverDto.setTitle(cover.getTitle());
        coverDto.setVideo(cover.getVideo());
        coverDto.setThumbnail(cover.getThumbnail());
        coverDto.setHit(cover.getHit());
        coverDto.setLikeCount(cover.getLikeCount());
        coverDto.setSongDto(SongDto.convertToDto(cover.getSong()));
//        coverDto.setCoverUserDtos(cover.getCoverUsers()
//                .stream().map(CoverUserDto::convertToDto)
//                .collect(Collectors.toList()));
//        coverDto.setCoverLikeDtos(cover.getCoverLikes()
//                .stream().map(CoverLikeDto::convertToDto)
//                .collect(Collectors.toList()));
        coverDto.setCoverReviewDtos(cover.getCoverReviews()
                .stream().map(CoverReviewDto::convertToDto)
                .collect(Collectors.toList()));
        return coverDto;
    }
    public static CoverDto convertToDtoList(Cover cover) {
        CoverDto coverDto = new CoverDto();
        // set
        coverDto.setId(cover.getId());
        coverDto.setTitle(cover.getTitle());
        coverDto.setVideo(cover.getVideo());
        coverDto.setThumbnail(cover.getThumbnail());
        coverDto.setHit(cover.getHit());
        coverDto.setLikeCount(cover.getLikeCount());
        coverDto.setCreatedAt(cover.getCreatedAt());
        coverDto.setSongDto(SongDto.convertToDto(cover.getSong()));
        return coverDto;
    }
    public static CoverDto convertToDtoSearch(Cover cover){
        CoverDto coverDto=new CoverDto();

        coverDto.setId(cover.getId());
        coverDto.setThumbnail(cover.getThumbnail());
        coverDto.setTitle(cover.getTitle());
        return coverDto;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
