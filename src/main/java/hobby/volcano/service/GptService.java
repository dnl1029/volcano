package hobby.volcano.service;

import hobby.volcano.config.GptConfig;
import hobby.volcano.config.GptFeignClient;
import hobby.volcano.config.ImgurFeignClient;
import hobby.volcano.dto.ContentDto;
import hobby.volcano.dto.GptRequestDto;
import hobby.volcano.dto.GptResponseDto;
import hobby.volcano.dto.ImgurResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GptService {

    private final GptConfig gptConfig;
    private final ImgurFeignClient imgurFeignClient;
    private final GptFeignClient gptFeignClient;

    @Value("${imgur.clientId}")
    private String clientId;

    @Value("${imgur.type}")
    private String type;

    @Value("${imgur.title}")
    private String title;

    @Value("${imgur.description}")
    private String description;

    @Value("${gpt.authorization}")
    private String authorization;

    public GptRequestDto createGptRequestWithImageUrl(String imageUrl) {
        List<GptRequestDto.Message> messages = gptConfig.getMessages().stream()
                .map(msg -> GptRequestDto.Message.builder()
                        .role(msg.getRole())
                        .content(msg.getContent().stream()
                                .map(content -> {
                                    GptRequestDto.Message.Content.ContentBuilder contentBuilder = GptRequestDto.Message.Content.builder()
                                            .type(content.getType())
                                            .text(content.getText());

                                    if ("image_url".equals(content.getType())) {
                                        contentBuilder.imageUrl(GptRequestDto.Message.Content.ImageUrl.builder()
                                                .url(imageUrl)
                                                .build());
                                    }

                                    return contentBuilder.build();
                                })
                                .filter(content -> content.getText() != null || content.getImageUrl() != null)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return GptRequestDto.builder()
                .model(gptConfig.getModel())
                .messages(messages)
                .build();
    }

    public ImgurResponseDto uploadImage(MultipartFile image) {
        return imgurFeignClient.uploadImage(clientId, type, title, description, image);
    }

    public String uploadImageAndGetLink(MultipartFile image) {
        ImgurResponseDto responseDto = uploadImage(image);
        return responseDto.getData().getLink();
    }

    public GptRequestDto upLoadImageAndGetGptRequest(MultipartFile image) {
        String imageUrl = uploadImageAndGetLink(image);
        GptRequestDto gptRequestDto = createGptRequestWithImageUrl(imageUrl);
        return gptRequestDto;
    }

    public GptResponseDto upLoadImageAndGetGptResponse(MultipartFile image) {
        GptRequestDto gptRequestDto = upLoadImageAndGetGptRequest(image);
        GptResponseDto gptResponseDto = gptFeignClient.getChatCompletion(authorization, gptRequestDto);
        return gptResponseDto;
    }

    public ContentDto extractContentFromResponse(MultipartFile image) {
        GptResponseDto gptResponseDto = upLoadImageAndGetGptResponse(image);

        if (gptResponseDto != null && gptResponseDto.getChoices() != null && !gptResponseDto.getChoices().isEmpty()) {
            Optional<GptResponseDto.Choice> firstChoice = gptResponseDto.getChoices().stream().findFirst();

            if (firstChoice.isPresent() && firstChoice.get().getMessage() != null) {
                String contentString = firstChoice.get().getMessage().getContent();
                return ContentDto.fromString(contentString);
            }
        }

        // Return an error DTO if the response is invalid or empty
        return new ContentDto("400", "No valid content found");
    }


}
