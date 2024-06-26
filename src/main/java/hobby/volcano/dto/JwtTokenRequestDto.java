package hobby.volcano.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenRequestDto {

    @Schema(description = "jwt토큰", example = "eyJ0eXAiOiJKV1...")
    private String jwtToken;

    @Builder
    public JwtTokenRequestDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
