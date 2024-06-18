package hobby.volcano.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "SCORE_RECORD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "WORK_DT")
    private String workDt;
    @Column(name = "USER_ID")
    private Integer userId;
//    @Column(name = "USER_NAME")
//    private String userName;
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

    @Builder
    public Score(Long id, String workDt, Integer userId, String attendance, Integer gameNum, Integer laneNum, Integer laneOrder, Integer score, LocalDateTime crtTm, String crtNm, LocalDateTime chgTm, String chgNm) {
        this.id = id;
        this.workDt = workDt;
        this.userId = userId;
//        this.userName = userName;
        this.attendance = attendance;
        this.gameNum = gameNum;
        this.laneNum = laneNum;
        this.laneOrder = laneOrder;
        this.score = score;
        this.crtTm = crtTm;
        this.crtNm = crtNm;
        this.chgTm = chgTm;
        this.chgNm = chgNm;
    }
}
