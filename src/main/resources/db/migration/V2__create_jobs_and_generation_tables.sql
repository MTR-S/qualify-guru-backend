-- V2: Creating the "Jobs & Generation" domain (Orchestration, AI, and Logs | RabbitMQ and LLM)

CREATE TABLE `jg_resumes_generation_requests` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `public_id` binary(16) NOT NULL,
    `user_id` bigint NOT NULL COMMENT 'Para listagem rápida por usuário sem JOIN',
    `profile_id` bigint NOT NULL COMMENT 'O perfil (currículo rascunho) utilizado',
    `job_description` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Texto da vaga colado pelo usuário',
    `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT 'Mapeado via Enum: PENDING, PROCESSING, COMPLETED, FAILED',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `public_id` (`public_id`),
    KEY `fk_generation_profile` (`profile_id`),
    KEY `idx_generation_public_id` (`public_id`),
    KEY `idx_generation_user_id` (`user_id`),
    KEY `idx_generation_status` (`status`),
    CONSTRAINT `fk_generation_profile` FOREIGN KEY (`profile_id`) REFERENCES `ip_user_profiles` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_generation_user` FOREIGN KEY (`user_id`) REFERENCES `ip_users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `jg_generation_request_logs` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `request_id` bigint NOT NULL COMMENT 'A qual requisição este log pertence',
    `status` varchar(50) NOT NULL COMMENT 'O status no momento do log (ex: PROCESSING, FAILED)',
    `message` text COMMENT 'Pode ser erro da stacktrace, aviso do rate limit do LLM, etc.',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data exata do evento (imutável)',
    PRIMARY KEY (`id`),
    KEY `idx_log_request_id` (`request_id`),
    CONSTRAINT `fk_log_request` FOREIGN KEY (`request_id`) REFERENCES `jg_resumes_generation_requests` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;