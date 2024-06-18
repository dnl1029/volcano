package hobby.volcano.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserScoreResponseDto {

    private String yearMonth;
    private Integer maxScore;
    private Double avgScore;

    @Builder
    public UserScoreResponseDto(String yearMonth, Integer maxScore, Double avgScore) {
        this.yearMonth = yearMonth;
        this.maxScore = maxScore;
        this.avgScore = avgScore;
    }
}
