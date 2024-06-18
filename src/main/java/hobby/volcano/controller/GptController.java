package hobby.volcano.controller;

import hobby.volcano.dto.*;
import hobby.volcano.service.GptService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @Operation(summary = "미사용 테스트 api")
    @PostMapping("/request")
    public ResponseEntity<GptRequestDto> createGptRequest(@RequestBody ImageUrlDto imageUrlDto) {
        GptRequestDto gptRequestDto = gptService.createGptRequestWithImageUrl(imageUrlDto.getImageUrl());
        return ResponseEntity.ok(gptRequestDto);
    }

    @Operation(summary = "미사용 테스트 api")
    @PostMapping("/upload")
    public ResponseEntity<ImgurResponseDto> uploadImage(@RequestParam("image") MultipartFile image) {
        ImgurResponseDto response = gptService.uploadImage(image);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "미사용 테스트 api")
    @PostMapping("/upload/gpt/request")
    public ResponseEntity<GptRequestDto> upLoadImageAndGetGptRequest(@RequestParam("image") MultipartFile image) {
        GptRequestDto gptRequestDto = gptService.upLoadImageAndGetGptRequest(image);
        return ResponseEntity.ok(gptRequestDto);
    }

    @Operation(summary = "gpt response api", description = "MultipartFile방식으로 post하여 이미지 업로드 시, gpt 응답을 전부 확인하고 싶을때 사용하는 api.")
    @PostMapping("/upload/gpt/response")
    public ResponseEntity<GptResponseDto> upLoadImageAndGetGptResponse(@RequestParam("image") MultipartFile image) {
        GptResponseDto gptResponseDto = gptService.upLoadImageAndGetGptResponse(image);
        return ResponseEntity.ok(gptResponseDto);
    }

    @Operation(summary = "gpt final content api", description = "MultipartFile방식으로 post하여 이미지 업로드 시, gpt 응답중에서 content(최종점수들) 파일만 파싱하여 불러오는 api.")
    @PostMapping("/upload/gpt/extract/content")
    public ResponseEntity<ContentDto> upLoadImageAndGetGptResponseAndContent(@RequestParam("image") MultipartFile image) {
        ContentDto contentDto = gptService.extractContentFromResponse(image);
        return ResponseEntity.ok(contentDto);
    }

}

