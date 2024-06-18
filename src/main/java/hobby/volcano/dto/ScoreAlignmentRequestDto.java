package hobby.volcano.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScoreAlignmentRequestDto {

    @Schema(description = "년월일", example = "20240617")
    private String workDt;
    @Schema(description = "사번", example = "12400454")
    private Integer userId;
    @Schema(description = "game번호", example = "1")
    private Integer gameNum;
    @Schema(description = "lane번호", example = "1")
    private Integer laneNum;
    @Schema(description = "lane내의 순서(order)", example = "3")
    private Integer laneOrder;
    @Schema(description = "볼링점수", example = "135")
    private Integer score;

    @Builder
    public ScoreAlignmentRequestDto(String workDt, Integer userId, Integer gameNum, Integer laneNum, Integer laneOrder, Integer score) {
        this.workDt = workDt;
        this.userId = userId;
        this.gameNum = gameNum;
        this.laneNum = laneNum;
        this.laneOrder = laneOrder;
        this.score = score;
    }
}
