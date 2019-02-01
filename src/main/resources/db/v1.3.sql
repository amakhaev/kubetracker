DROP TABLE IF EXISTS commands;

ALTER TABLE settings ADD COLUMN jenkins_api_token VARCHAR(256);

CREATE TABLE IF NOT EXISTS jenkins_jobs (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(128) NOT NULL,
  display_name VARCHAR(128) NOT NULL,
  build_number VARCHAR(16) NOT NULL,
  started_at DATETIME NOT NULL,
  duration INTEGER,
  estimate_duration INTEGER,
  result VARCHAR(32),
  previous_build_result VARCHAR(32),
  type VARCHAR(16) NOT NULL
);