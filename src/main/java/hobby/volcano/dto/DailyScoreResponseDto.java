package hobby.volcano.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyScoreResponseDto {

    private String workDt;
    private Integer userId;
    private String userName;
    private Integer gameNum;
    private Integer laneNum;
    private Integer laneOrder;
    private Integer score;

    @Builder
    public DailyScoreResponseDto(String workDt, Integer userId,String userName,Integer gameNum, Integer laneNum, Integer laneOrder, Integer score) {
        this.workDt = workDt;
        this.userId = userId;
        this.userName = userName;
        this.gameNum = gameNum;
        this.laneNum = laneNum;
        this.laneOrder = laneOrder;
        this.score = score;
    }
}
