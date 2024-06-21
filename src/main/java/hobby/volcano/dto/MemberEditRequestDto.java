package hobby.volcano.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditRequestDto {

    @Schema(description = "사번", example = "12400454")
    private Integer userId;
    @Schema(description = "이름", example = "위형규")
    private String userName;
    @Schema(description = "이미지 파일명", example = "1.png")
    private String imageFileName;
    @Schema(description = "계정상태", example = "Y")
    private String statusYn;
    @Schema(description = "계정역할/권한", example = "USER")
    private String role;

    @Builder
    public MemberEditRequestDto(Integer userId, String userName, String imageFileName, String statusYn, String role) {
        this.userId = userId;
        this.userName = userName;
        this.imageFileName = imageFileName;
        this.statusYn = statusYn;
        this.role = role;
    }
}
