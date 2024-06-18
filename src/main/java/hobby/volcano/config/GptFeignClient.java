package hobby.volcano.config;

import hobby.volcano.dto.GptRequestDto;
import hobby.volcano.dto.GptResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "gptFeignClient", url = "https://api.openai.com/v1")
public interface GptFeignClient {

    @PostMapping(value = "/chat/completions", consumes = "application/json")
    GptResponseDto getChatCompletion(
            @RequestHeader("Authorization") String authorization,
            @RequestBody GptRequestDto gptRequestDto
    );
}
