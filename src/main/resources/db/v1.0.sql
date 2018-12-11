CREATE TABLE IF NOT EXISTS settings (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  full_name NOT NULL UNIQUE,
  short_name NOT NULL UNIQUE,
  password NOT NULL UNIQUE
);

INSERT INTO settings (full_name, short_name, password)
    SELECT 'default', 'default', 'default' WHERE (SELECT count(*) FROM settings) = 0;

CREATE TABLE IF NOT EXISTS pod_filters (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  filter_value NOT NULL UNIQUE
);