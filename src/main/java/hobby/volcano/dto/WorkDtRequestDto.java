package hobby.volcano.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkDtRequestDto {

    @Schema(description = "년월일", example = "20240617")
    private String workDt;

    @Builder
    public WorkDtRequestDto(String workDt) {
        this.workDt = workDt;
    }
}
