ALTER TABLE settings RENAME TO _settings_old;

CREATE TABLE settings (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  kubernetes_name,
  jenkins_name,
  password,
  jenkins_api_token VARCHAR(256),
  unique (kubernetes_name, password),
  unique (jenkins_name, jenkins_api_token)
);

INSERT INTO settings (kubernetes_name, jenkins_name, password)
    SELECT 'default', 'default', 'default' WHERE (SELECT count(*) FROM settings) = 0;
	
DROP TABLE IF EXISTS _settings_old;
