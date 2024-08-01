package hobby.volcano.service;

import hobby.volcano.entity.Member;
import hobby.volcano.entity.MemberBackup;
import hobby.volcano.entity.Score;
import hobby.volcano.entity.ScoreBackup;
import hobby.volcano.repository.MemberBackupRepository;
import hobby.volcano.repository.MemberRepository;
import hobby.volcano.repository.ScoreBackupRepository;
import hobby.volcano.repository.ScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupService {

    private final MemberRepository memberRepository;
    private final ScoreRepository scoreRepository;
    private final MemberBackupRepository memberBackupRepository;
    private final ScoreBackupRepository scoreBackupRepository;

    @Transactional
    public void backupTables() {

        LocalDateTime backupStartTime = LocalDateTime.now();
        String backupDate = backupStartTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("백업 시작 시간 : {}",backupStartTime);

        // HOBBY_MEMBER 백업
        List<Member> members = memberRepository.findAll();
        List<MemberBackup> memberBackups = members.stream().map(member ->
                MemberBackup.builder()
                        .backupDt(backupDate)
                        .userId(member.getUserId())
                        .userName(member.getUserName())
                        .imageFileName(member.getImageFileName())
                        .statusYn(member.getStatusYn())
                        .role(member.getRole())
                        .crtTm(member.getCrtTm())
                        .crtNm(member.getCrtNm())
                        .chgTm(member.getChgTm())
                        .chgNm(member.getChgNm())
                        .backupTm(LocalDateTime.now())
                        .build()
        ).collect(Collectors.toList());
        memberBackupRepository.saveAll(memberBackups);

        // SCORE_RECORD 백업
        List<Score> scores = scoreRepository.findAll();
        List<ScoreBackup> scoreBackups = scores.stream().map(score ->
                ScoreBackup.builder()
                        .backupDt(backupDate)
                        .id(score.getId())
                        .workDt(score.getWorkDt())
                        .userId(score.getUserId())
                        .attendance(score.getAttendance())
                        .gameNum(score.getGameNum())
                        .laneNum(score.getLaneNum())
                        .laneOrder(score.getLaneOrder())
                        .score(score.getScore())
                        .crtTm(score.getCrtTm())
                        .crtNm(score.getCrtNm())
                        .chgTm(score.getChgTm())
                        .chgNm(score.getChgNm())
                        .backupTm(LocalDateTime.now())
                        .build()
        ).collect(Collectors.toList());
        scoreBackupRepository.saveAll(scoreBackups);

        LocalDateTime backupEndTime = LocalDateTime.now();
        log.info("백업 종료 시간 : {}",backupEndTime);
    }

}
