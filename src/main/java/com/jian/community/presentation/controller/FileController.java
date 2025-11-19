package com.jian.community.presentation.controller;

import com.jian.community.application.service.ImageService;
import com.jian.community.presentation.dto.ImageUrlResponse;
import com.jian.community.presentation.dto.UploadImageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "File", description = "파일 관련 API")
@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {

    private final ImageService imageService;

    @Operation(summary = "프로필 이미지 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = @ExampleObject(name = "유효하지 않은 이미지 형식", value = """
                                            {
                                                "code": "INVALID_IMAGE_FORMAT",
                                                "message": "지원하지 않는 이미지 형식입니다."
                                            }
                                            """))),
            @ApiResponse(responseCode = "413",
                    content = @Content(
                            examples = @ExampleObject(name = "이미지 용량 초과", value = """
                                            {
                                                "code": "IMAGE_TOO_LARGE",
                                                "message": "이미지 용량이 너무 큽니다. 5MB 이하로 업로드해주세요."
                                            }
                                            """)))})
    @PostMapping(value = "/profile-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ImageUrlResponse uploadProfileImages(@Valid @ModelAttribute UploadImageRequest request) {
        return imageService.upload(request.image());
    }
}
