-- V1: Creating a user "Identity & Profile" domain.

CREATE TABLE `ip_users` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `public_id` binary(16) NOT NULL COMMENT 'UUID v4 convertido para binário para performance',
    `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CLIENT' COMMENT 'Mapeado via Enum na API',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `public_id` (`public_id`),
    UNIQUE KEY `email` (`email`),
    KEY `idx_users_public_id` (`public_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ip_user_profiles` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `public_id` binary(16) NOT NULL,
    `user_id` bigint NOT NULL,
    `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Nome de identificação do perfil pelo usuário',
    `original_cv_s3_key` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Caminho do arquivo original no S3/LocalStack',
    `parsed_base_content` json DEFAULT NULL COMMENT 'Conteúdo base extraído para alimentar o LLM',
    `contact_metadata` json DEFAULT NULL COMMENT 'Flexibilidade para N links e contatos',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `public_id` (`public_id`),
    KEY `fk_user_profiles_user_id` (`user_id`),
    KEY `idx_user_profiles_public_id` (`public_id`),
    CONSTRAINT `fk_user_profiles_user_id` FOREIGN KEY (`user_id`) REFERENCES `ip_users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;