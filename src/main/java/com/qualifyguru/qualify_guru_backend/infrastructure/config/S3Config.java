package com.qualifyguru.qualify_guru_backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {
    // Configuration S3 Bucket at localhost usign LocalStack

    @Value("${aws.s3.endpoint:http://localhost:4566}")
    private String s3Endpoint;

    @Value("${aws.region:us-east-1}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(

                        AwsBasicCredentials.create("test", "test")
                ))
                .endpointOverride(URI.create(s3Endpoint))
                .forcePathStyle(true)
                .build();
    }
}
