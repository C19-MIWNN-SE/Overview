DROP DATABASE IF EXISTS overview;
CREATE DATABASE overview;

CREATE USER IF NOT EXISTS 'userOverview'@'localhost' IDENTIFIED BY 'userOverviewPW';
GRANT ALL ON overview.* TO 'userOverview'@'localhost';
FLUSH PRIVILEGES;