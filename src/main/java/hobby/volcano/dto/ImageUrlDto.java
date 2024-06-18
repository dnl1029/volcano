package hobby.volcano.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUrlDto {

    private String imageUrl;

    @Builder
    public ImageUrlDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
