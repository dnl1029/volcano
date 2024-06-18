package hobby.volcano.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "gpt")
@Getter
@Setter
public class GptConfig {
    private String model;
    private List<Message> messages;

    @Getter
    @Setter
    public static class Message {
        private String role;
        private List<Content> content;

        @Getter
        @Setter
        public static class Content {
            private String type;
            private String text;
            private String image_url;
        }
    }
}

