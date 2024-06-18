package hobby.volcano.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GptResponseDto {

    @JsonProperty("id")
    private String id;
    @JsonProperty("object")
    private String object;
    @JsonProperty("created")
    private long created;
    @JsonProperty("model")
    private String model;
    @JsonProperty("choices")
    private List<Choice> choices;
    @JsonProperty("usage")
    private Usage usage;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Choice {
        @JsonProperty("index")
        private int index;
        @JsonProperty("message")
        private Message message;
        @JsonProperty("logprobs")
        private Object logprobs; // assuming logprobs can be null or any other type
        @JsonProperty("finish_reason")
        private String finishReason;

        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        public static class Message {
            @JsonProperty("role")
            private String role;
            @JsonProperty("content")
            private String content;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }
}
