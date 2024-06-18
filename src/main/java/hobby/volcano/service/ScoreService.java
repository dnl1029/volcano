package hobby.volcano.service;

import hobby.volcano.common.CommonErrorCode;
import hobby.volcano.common.RestApiException;
import hobby.volcano.dto.*;
import hobby.volcano.entity.Member;
import hobby.volcano.entity.Score;
import hobby.volcano.repository.ScoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static hobby.volcano.common.CommonErrorCode.INVALID_PARAMETER;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final MemberService memberService;

    public List<Score> scoresByUserId(UserIdRequestDto userIdRequestDto) {
        List<Score> scoreListByUserId = scoreRepository.findByUserId(userIdRequestDto.getUserId());
        return scoreListByUserId;
    }

    public UserScoreResponseListDto userScoresByYearMonth(UserIdRequestDto userIdRequestDto) {
        List<Score> scoresByUserId = scoresByUserId(userIdRequestDto);
        Optional<Member> member = memberService.getMember(userIdRequestDto);

        Map<String, List<Score>> scoreListMapByYearMonth = scoresByUserId.stream()
                .filter(f -> f.getScore() != null) // null 값 제외
                .collect(Collectors.groupingBy(score -> score.getWorkDt().substring(0, 6)));

        List<UserScoreResponseDto> userScoreResponseDtoList = scoreListMapByYearMonth.entrySet().stream()
                .map(i -> {
                    String yearMonth = i.getKey();
                    List<Score> scores = i.getValue();

                    Integer maxScore = scores.stream()
                            .map(Score::getScore)
                            .max(Integer::compareTo)
                            .orElse(null);

                    Double avgScore = scores.stream()
                            .mapToInt(Score::getScore)
                            .average()
                            .orElse(Double.NaN);

                    return UserScoreResponseDto.builder()
                            .yearMonth(yearMonth)
                            .maxScore(maxScore)
                            .avgScore(avgScore.isNaN() ? null : avgScore)
                            .build();
                })
                .sorted(Comparator.comparing(UserScoreResponseDto::getYearMonth))
                .collect(Collectors.toList());

        return UserScoreResponseListDto.builder()
                .userId(userIdRequestDto.getUserId())
                .userName(member.get().getUserName())
                .scores(userScoreResponseDtoList)
                .build();

    }

    public RankingResponseDtoList ranking() {
        // 전체 Score 데이터를 가져오기
        List<Score> allScoreList = scoreRepository.findAll();

        // userId 별로 그룹화
        Map<Integer, List<Score>> scoresGroupedByUserId = allScoreList.stream()
                .filter(f -> f.getScore() != null) // null 값 제외
                .collect(Collectors.groupingBy(Score::getUserId));

        // 각 그룹에 대해 maxScore와 avgScore 계산
        List<RankingResponseDto> rankingResponseDtoList = scoresGroupedByUserId.entrySet().stream()
                .map(entry -> {
                    Integer tempUserId = entry.getKey();
                    Optional<Member> tempMember = memberService.getMember(UserIdRequestDto.builder().userId(tempUserId).build());
                    List<Score> scores = entry.getValue();

                    // 최대 점수 계산
                    Integer maxScore = scores.stream()
                            .map(Score::getScore)
                            .max(Integer::compareTo)
                            .orElse(0);

                    // 평균 점수 계산
                    Double avgScore = scores.stream()
                            .mapToInt(Score::getScore)
                            .average()
                            .orElse(0.0);

                    return RankingResponseDto.builder()
                            .userId(tempUserId)
                            .userName(tempMember.map(Member::getUserName).orElse("Unknown")) // userName 추가
                            .maxScore(maxScore)
                            .avgScore(avgScore.intValue())
                            .rankingByMaxScore(0) // 기본값 설정
                            .rankingByAvgScore(0) // 기본값 설정
                            .build();
                })
                .collect(Collectors.toList());

        // maxScore 기준으로 정렬하고 랭킹 매기기
        List<RankingResponseDto> rankedByMaxScore = assignRankingByScore(rankingResponseDtoList, Comparator.comparing(RankingResponseDto::getMaxScore).reversed(), "max");

        // avgScore 기준으로 정렬하고 랭킹 매기기
        List<RankingResponseDto> rankedByAvgScore = assignRankingByScore(rankedByMaxScore, Comparator.comparing(RankingResponseDto::getAvgScore).reversed(), "avg");

        return RankingResponseDtoList.builder()
                .rankings(rankedByAvgScore)
                .build();
    }

    private List<RankingResponseDto> assignRankingByScore(List<RankingResponseDto> rankingResponseDtoList, Comparator<RankingResponseDto> comparator, String type) {
        List<RankingResponseDto> sortedList = rankingResponseDtoList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        List<RankingResponseDto> rankedList = new ArrayList<>();
        int currentRank = 1;
        int sameRankCount = 0;
        RankingResponseDto previousDto = null;

        for (RankingResponseDto dto : sortedList) {
            if (previousDto != null && comparator.compare(dto, previousDto) != 0) {
                currentRank += sameRankCount;
                sameRankCount = 0;
            }

            sameRankCount++;

            RankingResponseDto newDto = RankingResponseDto.builder()
                    .userId(dto.getUserId())
                    .userName(dto.getUserName()) // userName 추가
                    .maxScore(dto.getMaxScore())
                    .avgScore(dto.getAvgScore())
                    .rankingByMaxScore(type.equals("max") ? currentRank : dto.getRankingByMaxScore())
                    .rankingByAvgScore(type.equals("avg") ? currentRank : dto.getRankingByAvgScore())
                    .build();

            rankedList.add(newDto);

            previousDto = newDto;
        }

        return rankedList;
    }


    public DailyScoreResponseDtoList dailyScoresAll() {
        List<Score> allScoreList = scoreRepository.findAll();
        List<DailyScoreResponseDto> scoreList = allScoreList.stream()
                .filter(f -> f.getScore() != null) // null 값 제외
                .map(i -> {
                    // userName 가져오기
                    String userName = memberService.getMember(UserIdRequestDto.builder().userId(i.getUserId()).build())
                            .map(Member::getUserName)
                            .orElse("Unknown"); // 유저가 존재하지 않으면 "Unknown"으로 처리

                    return DailyScoreResponseDto.builder()
                            .workDt(i.getWorkDt())
                            .userId(i.getUserId())
                            .userName(userName) // userName 추가
                            .gameNum(i.getGameNum())
                            .laneNum(i.getLaneNum())
                            .laneOrder(i.getLaneOrder())
                            .score(i.getScore())
                            .build();
                })
                .sorted(Comparator.comparing(DailyScoreResponseDto::getWorkDt)
                        .thenComparing(DailyScoreResponseDto::getUserId)
                        .thenComparing(DailyScoreResponseDto::getGameNum)
                        .thenComparing(DailyScoreResponseDto::getLaneNum)
                        .thenComparing(DailyScoreResponseDto::getLaneOrder))
                .collect(Collectors.toList());

        return DailyScoreResponseDtoList.builder()
                .dailyScores(scoreList)
                .build();
    }

    public DailyScoreResponseDtoList dailyScoresByWorkDt(WorkDtRequestDto workDtRequestDto) {
        List<Score> allScoreList = scoreRepository.findAll();
        List<DailyScoreResponseDto> scoreList = allScoreList.stream()
                .filter(f -> f.getScore() != null && f.getWorkDt().equals(workDtRequestDto.getWorkDt()))
                .map(i -> {
                    // userName 가져오기
                    String userName = memberService.getMember(UserIdRequestDto.builder().userId(i.getUserId()).build())
                            .map(Member::getUserName)
                            .orElse("Unknown"); // 유저가 존재하지 않으면 "Unknown"으로 처리

                    return DailyScoreResponseDto.builder()
                            .workDt(i.getWorkDt())
                            .userId(i.getUserId())
                            .userName(userName) // userName 추가
                            .gameNum(i.getGameNum())
                            .laneNum(i.getLaneNum())
                            .laneOrder(i.getLaneOrder())
                            .score(i.getScore())
                            .build();
                })
                .sorted(Comparator.comparing(DailyScoreResponseDto::getUserId)
                        .thenComparing(DailyScoreResponseDto::getGameNum)
                        .thenComparing(DailyScoreResponseDto::getLaneNum)
                        .thenComparing(DailyScoreResponseDto::getLaneOrder))
                .collect(Collectors.toList());

        return DailyScoreResponseDtoList.builder()
                .dailyScores(scoreList)
                .build();
    }


    public Score updateScoreAndInsertAlignment(ScoreAlignmentRequestDto scoreAlignmentRequestDto) {

        UserIdRequestDto userIdRequestDto = UserIdRequestDto.builder()
                .userId(scoreAlignmentRequestDto.getUserId())
                .build();

        Optional<Member> member = memberService.getMember(userIdRequestDto);
        if (member.isPresent()) {
            int tempGameNum = scoreAlignmentRequestDto.getGameNum() != null ? scoreAlignmentRequestDto.getGameNum() : 1;
//            int tempScore = scoreAlignmentRequestDto.getScore() != null ? scoreAlignmentRequestDto.getScore() : 0;
            Integer tempScore = scoreAlignmentRequestDto.getScore();

            // workDt, userId, gameNum 기준, 동일한 데이터가 있는지 확인
            Optional<Score> existingScore = scoreRepository.findByWorkDtAndUserIdAndGameNum(
                    scoreAlignmentRequestDto.getWorkDt(),
                    scoreAlignmentRequestDto.getUserId(),
                    tempGameNum
            );

            Score editedScore;
            if (existingScore.isPresent()) {
                // workDt, userId, gameNum 기준, 기존 데이터가 존재하면 수정
                Score scoreToUpdate = existingScore.get();
                log.info("updateScoreAndInsertAlignment. 수정 전 / workDt : {}, userId : {}, userName : {}, attendance : {}, gameNum : {}, laneNum : {}, laneOrder : {}, score : {}, crtTm : {}, crtNM : {}, chgTm : {}, chgNm : {}"
                        ,scoreToUpdate.getWorkDt()
                        ,scoreToUpdate.getUserId()
                        ,member.get().getUserName()
                        ,scoreToUpdate.getAttendance()
                        ,scoreToUpdate.getGameNum()
                        ,scoreToUpdate.getLaneNum()
                        ,scoreToUpdate.getLaneOrder()
                        ,scoreToUpdate.getScore()
                        ,scoreToUpdate.getCrtTm()
                        ,scoreToUpdate.getCrtNm()
                        ,scoreToUpdate.getChgTm()
                        ,scoreToUpdate.getChgNm());
                editedScore = Score.builder()
                        .id(scoreToUpdate.getId())
                        .workDt(scoreToUpdate.getWorkDt())
                        .userId(scoreToUpdate.getUserId())
//                        .userName(scoreToUpdate.getUserName())
                        .attendance(scoreToUpdate.getAttendance())
                        .gameNum(scoreToUpdate.getGameNum())
                        .laneNum(scoreAlignmentRequestDto.getLaneNum())
                        .laneOrder(scoreAlignmentRequestDto.getLaneOrder())
                        .score(tempScore) // 수정된 값 설정
                        .crtTm(scoreToUpdate.getCrtTm())
                        .crtNm(scoreToUpdate.getCrtNm())
                        .chgTm(LocalDateTime.now())
                        .chgNm(member.get().getUserName())
                        .build();
            } else {
                // 존재하지 않으면 새로운 데이터 추가
                Score score = Score.builder()
                        .workDt(scoreAlignmentRequestDto.getWorkDt())
                        .userId(scoreAlignmentRequestDto.getUserId())
//                        .userName(member.get().getUserName())
                        .attendance("Y")
                        .gameNum(tempGameNum) // 디폴트 1
                        .laneNum(scoreAlignmentRequestDto.getLaneNum())
                        .laneOrder(scoreAlignmentRequestDto.getLaneOrder())
                        .score(tempScore)
                        .crtTm(LocalDateTime.now())
                        .crtNm(member.get().getUserName())
                        .chgTm(LocalDateTime.now())
                        .chgNm(member.get().getUserName())
                        .build();
                editedScore = score;
            }

            editedScore = scoreRepository.save(editedScore); // 저장
            log.info("updateScoreAndInsertAlignment. 수정 후 / workDt : {}, userId : {}, userName : {}, attendance : {}, gameNum : {}, laneNum : {}, laneOrder : {}, score : {}, crtTm : {}, crtNM : {}, chgTm : {}, chgNm : {}"
                    ,editedScore.getWorkDt()
                    ,editedScore.getUserId()
                    ,member.get().getUserName()
                    ,editedScore.getAttendance()
                    ,editedScore.getGameNum()
                    ,editedScore.getLaneNum()
                    ,editedScore.getLaneOrder()
                    ,editedScore.getScore()
                    ,editedScore.getCrtTm()
                    ,editedScore.getCrtNm()
                    ,editedScore.getChgTm()
                    ,editedScore.getChgNm());
            return editedScore;
        } else {
            throw new RestApiException(CommonErrorCode.USER_NOT_FOUND);
        }
    }

    public Boolean determineAssignment(WorkDtRequestDto workDtRequestDto) {
        if (workDtRequestDto == null || workDtRequestDto.getWorkDt() == null) {
            throw new RestApiException(INVALID_PARAMETER);
        }

        List<Score> allScoreList = scoreRepository.findAll();
        boolean exists = allScoreList.stream()
                .anyMatch(score -> workDtRequestDto.getWorkDt().equals(score.getWorkDt()));

        return exists;
    }

}