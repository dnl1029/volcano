package hobby.volcano.service;

import hobby.volcano.common.ApiResponse;
import hobby.volcano.common.CommonErrorCode;
import hobby.volcano.common.RestApiException;
import hobby.volcano.dto.*;
import hobby.volcano.entity.Member;
import hobby.volcano.entity.Score;
import hobby.volcano.repository.ScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                            .imageFileName(tempMember.map(Member::getImageFileName).orElse("default.png"))
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
                    .imageFileName(dto.getImageFileName())
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
                //레인관리하기 화면에서 null로 입력된 초기데이터를 점수 기록하기화면에서 조회하기 위하여, null 제외조건 제거
//                .filter(f -> f.getScore() != null && f.getWorkDt().equals(workDtRequestDto.getWorkDt()))
                .filter(f -> f.getWorkDt().equals(workDtRequestDto.getWorkDt()))
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

    public boolean deleteScore(ScoreAlignmentRequestDto scoreAlignmentRequestDto) {
        Score score = scoreRepository.findByWorkDtAndUserIdAndGameNumAndLaneNumAndLaneOrder(
                scoreAlignmentRequestDto.getWorkDt()
                , scoreAlignmentRequestDto.getUserId()
                , scoreAlignmentRequestDto.getGameNum()
                , scoreAlignmentRequestDto.getLaneNum()
                , scoreAlignmentRequestDto.getLaneOrder()
        ).orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

        log.info("deleteScore. id : {}, workDt : {}, userId : {}, gameNum : {}, laneNum : {}, laneOrder : {}, score : {}"
                , score.getId()
                , score.getWorkDt()
                , score.getUserId()
                , score.getGameNum()
                , score.getLaneNum()
                , score.getLaneOrder()
                , score.getScore()
        );

        if(scoreRepository.existsById(score.getId())) {
            scoreRepository.deleteById(score.getId());
            return true;
        }
        else {
            return false;
        }
    }

    @Transactional
    public void deleteScoresByWorkDt(WorkDtRequestDto workDtRequestDto) {
        // workDt를 기준으로 점수 목록을 조회
        List<Score> scores = scoreRepository.findByWorkDt(workDtRequestDto.getWorkDt());

        // 삭제할 점수가 없으면 예외를 발생
        if (scores.isEmpty()) {
            throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }

        // 점수 목록을 삭제
        scoreRepository.deleteAll(scores);
        log.info("{}일자 score 삭제",workDtRequestDto.getWorkDt());
    }

    public MyProfileResponseDto getMyProfile(UserIdRequestDto userIdRequestDto) {
        Integer tempUserId = userIdRequestDto.getUserId();
        List<Score> scores = scoreRepository.findByUserId(tempUserId);
        Optional<Score> latestScore = scores.stream()
                .max(Comparator.comparing(Score::getWorkDt));
        String latestWorkDt =  latestScore.map(Score::getWorkDt).orElse(null);

        String imageFileName;
        Optional<Member> member = memberService.getMember(UserIdRequestDto.builder().userId(tempUserId).build());
        if (member.isPresent() && member.get().getImageFileName() != null) {
            imageFileName = member.get().getImageFileName();
        } else {
            imageFileName = "";
        }

        RankingResponseDtoList rankingResponseDtoList = ranking();
        MyProfileResponseDto myProfileResponseDto = rankingResponseDtoList.getRankings().stream()
                .filter(f -> f.getUserId().equals(tempUserId))
                .map(i ->
                        MyProfileResponseDto.builder()
                                .userId(i.getUserId())
                                .userName(i.getUserName())
                                .imageFileName(imageFileName)
                                .lastVisitDay(latestWorkDt)
                                .rankingByMaxScore(i.getRankingByMaxScore())
                                .rankingByAvgScore(i.getRankingByAvgScore())
                                .maxScore(i.getMaxScore())
                                .avgScore(i.getAvgScore())
                                .build()
                ).findFirst()
                .orElseThrow(() -> new RestApiException(INVALID_PARAMETER));

        return myProfileResponseDto;
    }

    public WorkDtResponseDtoList getScoreExistWorkDtList() {
        List<Score> allScoreList = scoreRepository.findAll();
        List<String> workDtList = allScoreList.stream()
//                .filter(f -> f.getScore() != null) // null 값 제외
                .map(Score::getWorkDt) // workDt 값만 추출
                .distinct()
                .sorted() // 오름차순 정렬
                .collect(Collectors.toList());

        return WorkDtResponseDtoList.builder()
                .workDtList(workDtList)
                .build();
    }

}
