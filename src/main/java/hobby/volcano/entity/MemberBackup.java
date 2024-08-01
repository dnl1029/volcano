package hobby.volcano.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "HOBBY_MEMBER_H")
@IdClass(MemberBackupId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBackup {

    @Id
    @Column(name = "BACKUP_DT")
    private String backupDt; // 'YYYYMMDD' 형식

    @Id
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "IMAGE_FILE_NAME")
    private String imageFileName;

    @Column(name = "STATUS_YN")
    private String statusYn;

    @Column(name = "ROLE")
    private String role;

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
    public MemberBackup(String backupDt, Integer userId, String userName, String imageFileName, String statusYn, String role, LocalDateTime crtTm, String crtNm, LocalDateTime chgTm, String chgNm, LocalDateTime backupTm) {
        this.backupDt = backupDt;
        this.userId = userId;
        this.userName = userName;
        this.imageFileName = imageFileName;
        this.statusYn = statusYn;
        this.role = role;
        this.crtTm = crtTm;
        this.crtNm = crtNm;
        this.chgTm = chgTm;
        this.chgNm = chgNm;
        this.backupTm = backupTm;
    }
}