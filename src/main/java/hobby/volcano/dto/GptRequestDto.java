package hobby.volcano.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GptRequestDto {

    @JsonProperty("model")
    private String model;
    @JsonProperty("messages")
    private List<Message> messages;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Message {
        @JsonProperty("role")
        private String role;
        @JsonProperty("content")
        private List<Content> content;

        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Content {
            @JsonProperty("type")
            private String type;
            @JsonProperty("text")
            private String text;
            @JsonProperty("image_url")
            private ImageUrl imageUrl;

            @Getter
            @Builder
            @NoArgsConstructor(access = AccessLevel.PROTECTED)
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class ImageUrl {
                @JsonProperty("url")
                private String url;
            }
        }
    }
}
