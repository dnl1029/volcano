package hobby.volcano.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "SCORE_RECORD_H")
@IdClass(ScoreBackupId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScoreBackup {

    @Id
    @Column(name = "BACKUP_DT")
    private String backupDt; // 'YYYYMMDD' 형식

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "WORK_DT")
    private String workDt;
    @Column(name = "USER_ID")
    private Integer userId;
    private String attendance;
    @Column(name = "GAME_NUM")
    private Integer gameNum;
    @Column(name = "LANE_NUM")
    private Integer laneNum;
    @Column(name = "LANE_ORDER")
    private Integer laneOrder;
    private Integer score;
    @Column(name = "CRT_TM")
    private LocalDateTime crtTm;
    @Column(name = "CRT_NM")
    private String crtNm;
    @Column(name = "CHG_TM")
    private LocalDateTime chgTm;
    @Column(name = "CHG_NM")
    private String chgNm;
    @Column(name = "BACKUP_TM")
    private LocalDateTime backupTm;

    @Builder
    public ScoreBackup(String backupDt,Long id, String workDt, Integer userId, String attendance, Integer gameNum, Integer laneNum, Integer laneOrder, Integer score, LocalDateTime crtTm, String crtNm, LocalDateTime chgTm, String chgNm, LocalDateTime backupTm) {
        this.backupDt = backupDt;
        this.id = id;
        this.workDt = workDt;
        this.userId = userId;
        this.attendance = attendance;
        this.gameNum = gameNum;
        this.laneNum = laneNum;
        this.laneOrder = laneOrder;
        this.score = score;
        this.crtTm = crtTm;
        this.crtNm = crtNm;
        this.chgTm = chgTm;
        this.chgNm = chgNm;
        this.backupTm = backupTm;
    }
}
