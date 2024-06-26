package hobby.volcano.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFileNameEditRequestDto {

    @Schema(description = "이미지 파일 이름", example = "1.png")
    private String imageFileName;

    @Builder
    public ImageFileNameEditRequestDto(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
