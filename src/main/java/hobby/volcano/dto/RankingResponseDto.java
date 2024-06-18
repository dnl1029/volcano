package hobby.volcano.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankingResponseDto {

    private Integer userId;
    private String userName;
    private Integer rankingByMaxScore;
    private Integer rankingByAvgScore;
    private Integer maxScore;
    private Integer avgScore;

    @Builder
    public RankingResponseDto(Integer userId,String userName, Integer rankingByMaxScore, Integer rankingByAvgScore, Integer maxScore, Integer avgScore) {
        this.userId = userId;
        this.userName = userName;
        this.rankingByMaxScore = rankingByMaxScore;
        this.rankingByAvgScore = rankingByAvgScore;
        this.maxScore = maxScore;
        this.avgScore = avgScore;
    }
}
