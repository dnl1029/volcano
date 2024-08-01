package hobby.volcano.entity;

import lombok.Data;

import java.io.Serializable;

// PK를 위한 복합 키 클래스
@Data
public class MemberBackupId implements Serializable {
    private String backupDt; // 'YYYYMMDD' 형식
    private Integer userId;
}