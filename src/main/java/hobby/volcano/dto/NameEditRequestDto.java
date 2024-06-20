package hobby.volcano.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NameEditRequestDto {

    @Schema(description = "이름", example = "위형규")
    private String userName;

    @Builder
    public NameEditRequestDto(String userName) {
        this.userName = userName;
    }
}
