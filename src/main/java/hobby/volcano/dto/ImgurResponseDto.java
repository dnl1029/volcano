package hobby.volcano.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImgurResponseDto {
    @JsonProperty("status")
    private int status;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("data")
    private DataDto data;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class DataDto {
        @JsonProperty("id")
        private String id;

        @JsonProperty("deletehash")
        private String deletehash;

        @JsonProperty("account_id")
        private String accountId;

        @JsonProperty("account_url")
        private String accountUrl;

        @JsonProperty("ad_type")
        private String adType;

        @JsonProperty("ad_url")
        private String adUrl;

        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private String description;

        @JsonProperty("name")
        private String name;

        @JsonProperty("type")
        private String type;

        @JsonProperty("width")
        private int width;

        @JsonProperty("height")
        private int height;

        @JsonProperty("size")
        private int size;

        @JsonProperty("views")
        private int views;

        @JsonProperty("section")
        private String section;

        @JsonProperty("vote")
        private String vote;

        @JsonProperty("bandwidth")
        private int bandwidth;

        @JsonProperty("animated")
        private boolean animated;

        @JsonProperty("favorite")
        private boolean favorite;

        @JsonProperty("in_gallery")
        private boolean inGallery;

        @JsonProperty("in_most_viral")
        private boolean inMostViral;

        @JsonProperty("has_sound")
        private boolean hasSound;

        //is_ad 필드 파싱에러로, "ad"필드가 추가로 나타나고 있어 getter setter 명시적으로 수정
//        @JsonProperty("is_ad")
//        private boolean isAd;
        private boolean isAd;

        @JsonProperty("is_ad")
        public boolean getIsAd() {
            return isAd;
        }

        @JsonProperty("is_ad")
        public void setIsAd(boolean isAd) {
            this.isAd = isAd;
        }

        @JsonProperty("nsfw")
        private String nsfw;

        @JsonProperty("link")
        private String link;

        @JsonProperty("tags")
        private String[] tags;

        @JsonProperty("datetime")
        private long datetime;

        @JsonProperty("mp4")
        private String mp4;

        @JsonProperty("hls")
        private String hls;
    }
}
