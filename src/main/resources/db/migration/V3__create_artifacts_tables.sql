-- V3: Creation of the "Artifacts & ATS" domain

CREATE TABLE `aa_generated_resumes` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `public_id` binary(16) NOT NULL,
    `request_id` bigint NOT NULL COMMENT 'Garante que cada job de IA tenha apenas um currículo final',
    `file_s3_key` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'URI do arquivo Markdown no bucket AWS S3',
    `file_format` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MARKDOWN' COMMENT 'Sinaliza para o client como processar o arquivo',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `public_id` (`public_id`),
    UNIQUE KEY `request_id` (`request_id`),
    CONSTRAINT `fk_generated_request` FOREIGN KEY (`request_id`) REFERENCES `jg_resumes_generation_requests` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;