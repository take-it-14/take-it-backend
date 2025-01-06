package com.takeit.s3.infrastructure.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.takeit.s3.infrastructure.util.dto.S3UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUpload {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 폴더 생성
    public void createFolder(String folderName) {
        amazonS3.putObject(bucket, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }

    // 다중 파일 업로드
    public List<S3UploadFile> uploadMultipleFile(List<MultipartFile> files, String folderName) throws IOException {

        // 지정된 폴더 및 날짜별 하위 폴더 생성
        String dateFolder = folderName + "/" + LocalDate.now();
        createFolder(folderName);
        createFolder(dateFolder);

        List<S3UploadFile> uploadFiles = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            // 원본 파일 이름에서 확장자 추출
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }


            // UUID로 고유한 파일 이름 생성
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            log.info("filename : {}", uniqueFilename);
            // 최종 경로 설정 (날짜 폴더 아래)
            String fullPath = dateFolder + "/" + uniqueFilename;
            log.info("fullPath : {}", fullPath);

            String encodedFullPath = URLEncoder.encode(fullPath, StandardCharsets.UTF_8);

            // 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());
//            metadata.setHeader("filename", multipartFile.getOriginalFilename());

            // S3에 파일 업로드
            amazonS3.putObject(bucket, encodedFullPath, multipartFile.getInputStream(), metadata);
            String fileUrl = amazonS3.getUrl(bucket, encodedFullPath).toString();

            uploadFiles.add(new S3UploadFile(uniqueFilename, fileUrl));
        }

        return uploadFiles; // 각 파일의 S3 URL을 리스트로 반환
    }

    // 단일 파일 업로드
    public S3UploadFile uploadSingleFile(MultipartFile file, String folderName) throws IOException {
        // 지정된 폴더 및 날짜별 하위 폴더 생성
        String dateFolder = folderName + "/" + LocalDate.now();
        createFolder(folderName);
        createFolder(dateFolder);

        // 원본 파일 이름에서 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // UUID로 고유한 파일 이름 생성
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // 최종 경로 설정 (날짜 폴더 아래)
        String fullPath = dateFolder + "/" + uniqueFilename;

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        metadata.setHeader("filename", file.getOriginalFilename());

        // S3에 파일 업로드
        amazonS3.putObject(bucket, fullPath, file.getInputStream(), metadata);
        String fileUrl = amazonS3.getUrl(bucket, fullPath).toString();

        return new S3UploadFile(uniqueFilename, fileUrl);
    }

    // 단일 파일 삭제
    public void fileDelete(S3UploadFile file) {
        if(amazonS3 != null) {
            amazonS3.deleteObject(new DeleteObjectRequest(file.uri(), file.filename()));
        } else {
            log.error("s3 connected error");
            throw new IllegalStateException("S3 client is not initialized");
        }
    }

    // 다중 파일 삭제
    public void fileDelete(List<S3UploadFile> files) {
        if(amazonS3 != null) {
            for(S3UploadFile file : files) {
                amazonS3.deleteObject(new DeleteObjectRequest(file.uri(), file.filename()));
            }
        } else {
            log.error("s3 connected error");
            throw new IllegalStateException("S3 client is not initialized");
        }
    }

}

