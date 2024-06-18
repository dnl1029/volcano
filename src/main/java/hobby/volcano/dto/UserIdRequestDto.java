package hobby.volcano.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserIdRequestDto {

    @Schema(description = "사번", example = "12400454")
    private Integer userId;

    @Builder
    public UserIdRequestDto(Integer userId) {
        this.userId = userId;
    }
}
