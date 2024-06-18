package hobby.volcano.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "HOBBY_MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "IMAGE_FILE_NAME")
    private String imageFileName;
    @Column(name = "STATUS_YN")
    private String statusYn;
    private String role;
    @Column(name = "CRT_TM")
    private LocalDateTime crtTm;
    @Column(name = "CRT_NM")
    private String crtNm;
    @Column(name = "CHG_TM")
    private LocalDateTime chgTm;
    @Column(name = "CHG_NM")
    private String chgNm;

    @Builder
    public Member(Integer userId, String userName, String imageFileName, String statusYn, String role, LocalDateTime crtTm, String crtNm, LocalDateTime chgTm, String chgNm) {
        this.userId = userId;
        this.userName = userName;
        this.imageFileName = imageFileName;
        this.statusYn = statusYn;
        this.role = role;
        this.crtTm = crtTm;
        this.crtNm = crtNm;
        this.chgTm = chgTm;
        this.chgNm = chgNm;
    }
}
