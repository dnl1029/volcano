package hobby.volcano.config;

import hobby.volcano.dto.ImgurResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "imgurFeignClient", url = "https://api.imgur.com/3")
public interface ImgurFeignClient {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ImgurResponseDto uploadImage(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("type") String type,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("image") MultipartFile image);

}

