package hobby.volcano.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyScoreResponseDtoList {

    private List<DailyScoreResponseDto> dailyScores;

    @Builder
    public DailyScoreResponseDtoList(List<DailyScoreResponseDto> dailyScores) {
        this.dailyScores = dailyScores;
    }
}
