DROP SCHEMA IF EXISTS weatherdb;
CREATE SCHEMA weatherdb;
USE weatherdb;

Drop TABLE IF EXISTS addresses;

CREATE TABLE addresses (
    address_ID int NOT NULL auto_increment,
    lat float,
    lng float,
    city varchar(15) NOT NULL,
    state varchar(2) NOT NULL,
    PRIMARY KEY (address_ID) 
);

INSERT INTO addresses (city, state) VALUES ('NewHaven', 'CT');
INSERT INTO addresses (city, state) VALUES ('Chicago', 'IL');
INSERT INTO addresses (city, state) VALUES ('Seattle', 'WA');
INSERT INTO addresses (city, state) VALUES ('Phoenix', 'AZ');
INSERT INTO addresses (city, state) VALUES ('Charlotte', 'NC');

Drop TABLE IF EXISTS CurrentWeather;

CREATE TABLE CurrentWeather(
	address_ID int NOT NULL,
    temp float,
    pressure float,
    humidity INT,
    temp_min float,
    temp_max float,
    primary key(address_ID),
    foreign key(address_ID) references addresses(address_ID)
    );

Drop TABLE IF EXISTS ForecastWeather;

CREATE TABLE ForecastWeather(
	address_ID int NOT NULL,
	forecast_time varchar(30) NOT NULL,
    temp float,
    pressure float,
    humidity INT,
    temp_min float,
    temp_max float,
    primary key(address_ID, forecast_time),
    foreign key(address_ID) references addresses(address_ID)
    );

SET @@global.time_zone = '+00:00';
SET @@session.time_zone = '+00:00';