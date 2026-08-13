package com.space.airesumeanalyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * MultipartFile을 AWS S3 버킷에 업로드하고 객체 URL을 반환합니다.
     */
    public String uploadFile(MultipartFile multipartFile) {
        // 1. 안전한 고유 파일명 생성 (예: uuid-원본파일.pdf)
        String originalFilename = multipartFile.getOriginalFilename();
        String safeFileName = UUID.randomUUID().toString() + "-" + originalFilename;

        try {
            // 2. S3 업로드 요청 객체 조립 (어느 버킷에, 어떤 이름으로 넣을지 설정)
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(safeFileName)
                    .contentType(multipartFile.getContentType())
                    .build();

            // 3. AWS S3로 파일 스트림 전송
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            log.info("AWS S3 업로드 성공: {}", safeFileName);

            // 4. 업로드된 파일의 접속 URL 획득 및 반환
            return getS3FileUrl(safeFileName);

        } catch (IOException e) {
            log.error("S3 파일 업로드 중 IO 예외 발생", e);
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    //S3에 저장된 객체의 고유 URL을 조회
    private String getS3FileUrl(String fileName) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.utilities().getUrl(request).toString();
    }
}