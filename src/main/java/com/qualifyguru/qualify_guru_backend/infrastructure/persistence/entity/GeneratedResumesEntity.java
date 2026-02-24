package com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity;

import com.qualifyguru.qualify_guru_backend.domain.model.FileFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "AA_GENERATED_RESUMES")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class GeneratedResumesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "public_id", updatable = false, nullable = false, unique = true)
    private UUID publicId = UUID.randomUUID();

    @Column(name = "file_s3_key", nullable = false, length = 512)
    private String fileS3Key;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", referencedColumnName = "id", nullable = false, unique = true)
    private ResumeGenerationRequestsEntity request;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_format", nullable = false, length = 50)
    private FileFormat fileFormat = FileFormat.MARKDOWN;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
