-- =============================================================================
-- TellMe — Flyway Migration V1: Initial Schema
-- =============================================================================
-- This script creates all tables for a fresh TellMe installation.
--
-- EXISTING INSTALLATIONS (schema already created by Hibernate):
--   Set the following in your application.properties or environment variables:
--     spring.flyway.enabled=true
--     spring.flyway.baseline-on-migrate=true
--     spring.flyway.baseline-version=0
--   This tells Flyway to treat the existing schema as baseline V0 and only
--   apply future migrations (V2 onwards) without re-running V1.
-- =============================================================================

-- Status lifecycle values (referenced by aspirasi.status_id)
CREATE TABLE IF NOT EXISTS status (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_status VARCHAR(100) NOT NULL,
    CONSTRAINT uq_status_nama UNIQUE (nama_status)
);

-- Submission categories (Akademik, Organisasi, etc.)
CREATE TABLE IF NOT EXISTS kategori (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_kategori VARCHAR(100) NOT NULL,
    CONSTRAINT uq_kategori_nama UNIQUE (nama_kategori)
);

-- User accounts
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama     VARCHAR(255) NOT NULL,
    nim      VARCHAR(50),
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL,
    token    VARCHAR(255),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT uq_users_nim   UNIQUE (nim),
    CONSTRAINT uq_users_token UNIQUE (token)
);

-- Student submissions (aspirasi = aspiration/feedback/complaint)
CREATE TABLE IF NOT EXISTS aspirasi (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    isi_aspirasi TEXT         NOT NULL,
    tanggal      DATETIME(6)  NOT NULL,
    anonim       TINYINT(1)   NOT NULL DEFAULT 0,
    email_tujuan VARCHAR(255),
    feedback     TEXT,
    user_id      BIGINT       NOT NULL,
    kategori_id  BIGINT,
    status_id    BIGINT       NOT NULL,
    CONSTRAINT fk_aspirasi_user     FOREIGN KEY (user_id)     REFERENCES users(id)    ON DELETE CASCADE,
    CONSTRAINT fk_aspirasi_kategori FOREIGN KEY (kategori_id) REFERENCES kategori(id) ON DELETE SET NULL,
    CONSTRAINT fk_aspirasi_status   FOREIGN KEY (status_id)   REFERENCES status(id)
);

-- Uploaded attachment paths for aspirasi
CREATE TABLE IF NOT EXISTS aspirasi_foto (
    aspirasi_id BIGINT       NOT NULL,
    foto_path   VARCHAR(500),
    CONSTRAINT fk_foto_aspirasi FOREIGN KEY (aspirasi_id) REFERENCES aspirasi(id) ON DELETE CASCADE
);

-- Forum discussion posts
CREATE TABLE IF NOT EXISTS forum_post (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    isi_post TEXT        NOT NULL,
    tanggal  DATETIME(6) NOT NULL,
    user_id  BIGINT      NOT NULL,
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Forum comments and threaded replies
CREATE TABLE IF NOT EXISTS forum_comment (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    isi_komentar      TEXT        NOT NULL,
    tanggal           DATETIME(6) NOT NULL,
    post_id           BIGINT      NOT NULL,
    user_id           BIGINT      NOT NULL,
    parent_comment_id BIGINT,
    CONSTRAINT fk_comment_post   FOREIGN KEY (post_id)           REFERENCES forum_post(id)    ON DELETE CASCADE,
    CONSTRAINT fk_comment_user   FOREIGN KEY (user_id)           REFERENCES users(id)         ON DELETE CASCADE,
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_comment_id) REFERENCES forum_comment(id) ON DELETE CASCADE
);
