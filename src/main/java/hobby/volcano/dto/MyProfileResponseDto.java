package hobby.volcano.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyProfileResponseDto {
    private Integer userId;
    private String userName;
    private String imageFileName;
    private Integer rankingByMaxScore;
    private Integer rankingByAvgScore;
    private Integer maxScore;
    private Integer avgScore;
    private String lastVisitDay;

    @Builder
    public MyProfileResponseDto(Integer userId, String userName, String imageFileName, Integer rankingByMaxScore, Integer rankingByAvgScore, Integer maxScore, Integer avgScore, String lastVisitDay) {
        this.userId = userId;
        this.userName = userName;
        this.imageFileName = imageFileName;
        this.rankingByMaxScore = rankingByMaxScore;
        this.rankingByAvgScore = rankingByAvgScore;
        this.maxScore = maxScore;
        this.avgScore = avgScore;
        this.lastVisitDay = lastVisitDay;
    }
}
