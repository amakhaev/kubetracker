DROP TABLE IF EXISTS commands;

ALTER TABLE settings ADD COLUMN jenkins_api_token VARCHAR(256);