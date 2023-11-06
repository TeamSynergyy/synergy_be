package com.seoultech.synergybe.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.seoultech.synergybe.domain.image.Image;
import com.seoultech.synergybe.domain.image.dto.Name;
import com.seoultech.synergybe.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile 로 데이터 받아와서 S3에 저장
    public List<Image> storeImageList(List<MultipartFile> files) {
        try {
            List<Name> collect = new ArrayList<>();

            for (MultipartFile file : files) {
                Name name = this.upload(file, "image");

                collect.add(name);
            }
            List<Image> images = collect.stream()
                    .map(
                            (name) -> new Image(name.getOriginalFilename(), name.getStoreFileName()))
                    .collect(Collectors.toList());
            return imageRepository.saveAll(images);
        }catch (Exception e) {
            log.error("create image {}", e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }




    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public Name upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    /**
     * fileName은 dirName과 fileName 즉 실제 파일 이름을 가져와 저장한다
     * 이렇게 되면 중복될 가능성이 있다
     * uploadFile은 실제 업로드를 할 파일이다 즉 이름과는 상관이 없다
     * 중복되지 않은 이름의 이미지를 생성하기 위해서는 fileName을 하는 과정에서 uploadFile에서
     * 중복을 제거하는 로직이 들어가면 될 것이다.
     */
    private Name upload(File uploadFile, String dirName) {
        String originalFilename = uploadFile.getName();
        String storeFilename = createStoreFileName(originalFilename);
        String fileName = dirName + "/" + storeFilename;
        String uploadImageUrl = putS3(uploadFile, fileName);

        Name name = new Name(originalFilename, uploadImageUrl);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return name;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public List<String> getImageUrlByPostId(Long postId) {
        return imageRepository.findStoreFileNamesByPostId(postId);
    }
}
