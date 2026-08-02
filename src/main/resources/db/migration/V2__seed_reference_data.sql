-- =============================================================================
-- TellMe — Flyway Migration V2: Seed Reference Data
-- =============================================================================
-- Inserts the default status lifecycle values and default categories.
-- Uses INSERT IGNORE to be idempotent (safe to re-run on existing data).
--
-- STATUS IDs are referenced by ID in the service layer:
--   ID 1 = Pending   (default for new submissions)
--   ID 2 = In Review
--   ID 3 = Resolved
--   ID 4 = Rejected
--
-- Changing the order or IDs of these rows will break status transitions.
-- To rename statuses, update the namaStatus values only, not the IDs.
-- =============================================================================

-- Default lifecycle statuses (DO NOT reorder — IDs are referenced by service layer)
INSERT IGNORE INTO status (id, nama_status) VALUES
    (1, 'Pending'),
    (2, 'In Review'),
    (3, 'Resolved'),
    (4, 'Rejected');

-- Default submission categories
-- Add or remove rows to match your institution's needs.
INSERT IGNORE INTO kategori (id, nama_kategori) VALUES
    (1, 'Akademik'),
    (2, 'Organisasi'),
    (3, 'Fasilitas'),
    (4, 'Lainnya');
