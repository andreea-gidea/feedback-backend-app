
-- DROP THE project_subscriber table --
DROP TABLE IF EXISTS project_subscriber;

-- DROP THE subscriber table --
DROP TABLE IF EXISTS subscriber;

-- DROP COLUMN password from app_user  --
ALTER TABLE IF EXISTS app_user DROP COLUMN password;

-- DROP COLUMN is_owner from project_app_user  --
ALTER TABLE IF EXISTS project_app_user DROP COLUMN is_owner;



