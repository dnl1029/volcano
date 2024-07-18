package hobby.volcano.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkDtResponseDtoList {

    @Schema(description = "년월일 리스트", example = "[\"20240617\", \"20240618\"]")
    private List<String> workDtList;

    @Builder
    public WorkDtResponseDtoList(List<String> workDtList) {
        this.workDtList = workDtList;
    }
}
