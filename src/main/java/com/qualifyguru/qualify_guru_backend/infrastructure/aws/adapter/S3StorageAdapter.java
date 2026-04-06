package com.qualifyguru.qualify_guru_backend.infrastructure.aws.adapter;

import com.qualifyguru.qualify_guru_backend.application.port.out.FileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Component
public class S3StorageAdapter implements FileStoragePort {

    private final S3Client s3Client;
    private final String bucketName;

    public S3StorageAdapter(S3Client s3Client,
                            @Value("${aws.s3.bucket.base-cv:qualify-guru-base-cvs}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadAndReturnKey(String originalFileName, InputStream content, String contentType, long contentLength) {

        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName.replaceAll("\\s+", "_");

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(content, contentLength));

        return uniqueFileName;
    }

    @Override
    public InputStream downloadFile(String fileKey) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        return s3Client.getObject(getObjectRequest);
    }
}
