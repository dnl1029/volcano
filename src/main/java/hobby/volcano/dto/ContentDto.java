package hobby.volcano.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ContentDto {

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("content")
    private Object content;

    public static ContentDto fromString(String contentString) {
        if (contentString.startsWith("[") && contentString.endsWith("]")) {
            // Process as integer array
            contentString = contentString.replaceAll("[\\[\\]\\s]", ""); // Remove brackets and spaces
            String[] contentArray = contentString.split(",");
            List<Integer> contentList = Arrays.stream(contentArray)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            return new ContentDto("200", contentList);
        } else {
            // Process as error message
            return new ContentDto("400", contentString);
        }
    }
}