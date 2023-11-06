package com.seoultech.synergybe.domain.image.controller;

import com.seoultech.synergybe.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

}
