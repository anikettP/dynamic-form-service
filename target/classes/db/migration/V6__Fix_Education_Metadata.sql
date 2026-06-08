-- V6: Fix Education Form Metadata
-- Remove fields that are commented-out in EducationRecordRequest DTO
-- (name, rank, unit) — they exist in metadata but not in the DTO,
-- causing DynamicValidationService to throw "Employee Name is required" on every POST.

DELETE FROM form_field_metadata
WHERE form_id = (SELECT id FROM form_metadata WHERE form_name = 'education')
  AND field_name IN ('name', 'rank', 'unit');
