package hobby.volcano.controller;

import hobby.volcano.common.ApiResponse;
import hobby.volcano.common.CustomEnum;
import hobby.volcano.dto.*;
import hobby.volcano.entity.Score;
import hobby.volcano.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Slf4j
public class ScoreController {

    private final ScoreService scoreService;

    @Operation(summary = "get My score by my userId api", description = "jwt토큰에서 userId를 읽어 score를 return. 내 월별점수 화면에서 사용하는 api.")
    @GetMapping("score/myUserId")
    public UserScoreResponseListDto getMyUserScoresByYearMonth() {
        String myUserId = MDC.get(CustomEnum.USER_ID.getContent());
        UserScoreResponseListDto userScoreResponseListDto = scoreService.userScoresByYearMonth(UserIdRequestDto.builder().userId(Integer.valueOf(myUserId)).build());
        return userScoreResponseListDto;
    }

    @Operation(summary = "미사용 api / get score by userId api", description = "userId입력시 score를 return. 내 월별점수 화면에서 사용하는 api.")
    @PostMapping("score/userId")
    public UserScoreResponseListDto getUserScoresByYearMonth(@RequestBody UserIdRequestDto userIdRequestDto) {
        UserScoreResponseListDto userScoreResponseListDto = scoreService.userScoresByYearMonth(userIdRequestDto);
        return userScoreResponseListDto;
    }

    @Operation(summary = "get ranking api", description = "취미반의 랭킹을 return. 취미반랭킹 화면에서 사용하는 api.")
    @GetMapping("score/ranking")
    public RankingResponseDtoList getRanking() {
        RankingResponseDtoList rankingResponseDtoList = scoreService.ranking();
        return rankingResponseDtoList;
    }

    @Operation(summary = "미사용 api / 스코어 전체조회 api")
    @GetMapping("score/daily/all")
    public DailyScoreResponseDtoList getDailyScoresAll() {
        DailyScoreResponseDtoList dailyScoreResponseDtoList = scoreService.dailyScoresAll();
        return dailyScoreResponseDtoList;
    }

    @Operation(summary = "get scores by workDt api", description = "일자를 입력시 해당 일자의 점수기록 return. 취미반 볼링 기록 화면에서 사용하는 api.")
    @PostMapping("score/daily/workDt")
    public DailyScoreResponseDtoList getDailyScoresByWorkDt(@RequestBody WorkDtRequestDto workDtRequestDto) {
        DailyScoreResponseDtoList dailyScoreResponseDtoList = scoreService.dailyScoresByWorkDt(workDtRequestDto);
        return dailyScoreResponseDtoList;
    }

    @Operation(summary = "create lane alignment api", description = "workDt, userId, gameNum, laneNum, laneOrder, score를 입력하는 api. 레인관리하기 화면에서 사용시 score를 null값으로 입력하고, 점수 기록하기 화면에서 사용시 score를 입력하여 점수를 update.")
    @PostMapping("score/update/andInsert")
    public Score updateScoreAndInsertAlignment(@RequestBody ScoreAlignmentRequestDto scoreAlignmentRequestDto) {
        Score score = scoreService.updateScoreAndInsertAlignment(scoreAlignmentRequestDto);
        return score;
    }

    @Operation(summary = "determine alignment api", description = " workDt입력 시, 초기에 입력된 레인순서 정보가 있는지 판단. 점수기록하기 화면에서 사용하는 api.")
    @PostMapping("score/determine/alignment")
    public boolean determineAssignment(@RequestBody WorkDtRequestDto workDtRequestDto) {
        Boolean result = scoreService.determineAssignment(workDtRequestDto);
        return result;
    }

    @Operation(summary = "delete score api", description = "score 삭제. workDt, userId, gameNum, laneNum, laneOrder, score를 입력하여 삭제하는 api")
    @PostMapping("score/delete")
    public ApiResponse deleteScore(@RequestBody ScoreAlignmentRequestDto scoreAlignmentRequestDto) {
        boolean res = scoreService.deleteScore(scoreAlignmentRequestDto);
        if(res) {
            return ApiResponse.builder().code("200").message("score가 정상적으로 삭제되었습니다.").build();
        }
        else {
            return ApiResponse.builder().code("400").message("score가 삭제되지 않았습니다.").build();
        }
    }

    @Operation(summary = "delete score by workDt api", description = "workDt에 해당하는 score들을 전부 삭제하는 api")
    @PostMapping("score/delete/workDt")
    public ApiResponse deleteScoresByWorkDt(@RequestBody WorkDtRequestDto workDtRequestDto) {
        scoreService.deleteScoresByWorkDt(workDtRequestDto);
        return ApiResponse.builder().code("200").message("score들이 정상적으로 삭제되었습니다.").build();
    }

    @Operation(summary = "get workDt List", description = "캘린더에 표시 목적으로 볼링 점수가 있는 날짜 리스트를 return하는 api.")
    @GetMapping("score/workDtList")
    public WorkDtResponseDtoList getScoreExistWorkDtList() {
        WorkDtResponseDtoList scoreExistWorkDtList = scoreService.getScoreExistWorkDtList();
        return scoreExistWorkDtList;
    }

}
