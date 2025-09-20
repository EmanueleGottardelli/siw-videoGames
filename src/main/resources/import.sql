-- Inserimento utenti
INSERT INTO users (id, name, surname, email) VALUES (nextval('users_seq'),'Emanuele', 'Gottardelli', 'ema.gotta@ex.it')
INSERT INTO users (id,name, surname, email) VALUES (nextval('users_seq'),'Franca', 'Verdi', 'franca.v1998@ex.com');

-- Inserimento developer
INSERT INTO developer (id,name, nationality, date_of_birth) VALUES (nextval('developer_seq'),'EA','Canada','1982-05-28');
INSERT INTO developer (id,name, nationality, date_of_birth) VALUES (nextval('developer_seq'),'Activision','USA','1979-10-01');

-- Inserimento videogame
INSERT INTO videogame (id,title, release_date, genre, platform, description, publisher_id) VALUES (nextval('videogame_seq'),'Fifa 15', '2014-09-19','Sportivo','Console','Gioco di simulazione calcistica',51)
INSERT INTO videogame (id,title, release_date, genre, platform, description, publisher_id) VALUES (nextval('videogame_seq'),'COD 4', '2007-11-05','Sparatutto','Console','Simulazione di guerra',1);

-- Inserimento credenziali
INSERT INTO credentials (id,username, password, role, user_id) VALUES (nextval('credentials_seq'),'SuperAdmin', '$2b$12$sMe4cEfpceel5ijYC5gWh.m.tMs0rT2pOCtvj99B11GzNg7Fara/C', 'ADMIN', 1);
INSERT INTO credentials (id,username, password, role, user_id) VALUES (nextval('credentials_seq'),'Franca', '$2b$12$sMe4cEfpceel5ijYC5gWh.m.tMs0rT2pOCtvj99B11GzNg7Fara/C', 'DEFAULT', 51);