-- drop table if exists USERS;
-- drop table if exists USER_ROLE;
-- drop table if exists GENRES;
-- drop table if exists BOOK_AUTHORS;
-- drop table if exists BOOKS;
-- drop table if exists AUTHORS;

-- CREATE SEQUENCE global_seq START WITH 1;

-- create table USERS
-- (
--     ID         INTEGER AUTO_INCREMENT PRIMARY KEY,
--     EMAIL      VARCHAR not null,
--     FIRST_NAME VARCHAR not null,
--     LAST_NAME  VARCHAR not null,
--     PASSWORD   VARCHAR not null
-- );
INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_Last', '{noop}admin');

-- create table USER_ROLE
-- (
--     ROLE    VARCHAR not null,
--     USER_ID INTEGER not null
-- );
INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

-- create table BOOKS
-- (
--     ID        INTEGER AUTO_INCREMENT PRIMARY KEY,
--     TITLE     VARCHAR not null,
--     FINISHED  BOOLEAN not null,
--     OWNED     BOOLEAN not null,
--     BOOK_TYPE VARCHAR not null
-- );
INSERT INTO BOOKS (TITLE, FINISHED, OWNED, BOOK_TYPE)
VALUES ('Moby Dick', true, true, 'PAPER'),
       ('Best served cold', true, false, 'NONE'),
       ('Всем стоять на Занзибаре', false, true, 'PAPER'),
       ('Герои', false, true, 'PAPER'),
       ('Благие знамения', true, true, 'PAPER'),
       ('Стража! Стража!', true, true, 'PAPER'),
       ('Абсолютист', true, true, 'PAPER');

-- create table GENRES
-- (
--     BOOK_ID INTEGER not null,
--     GENRE   VARCHAR not null
-- );
INSERT INTO GENRES (BOOK_ID, GENRE)
VALUES (1, 'CLASSICS'),
       (2, 'FANTASY'),
       (3, 'SF'),
       (4, 'FANTASY'),
       (5, 'FANTASY'),
       (6, 'FANTASY'),
       (7, 'MODERN_LIT'),
       (7, 'WAR');

-- create table AUTHORS
-- (
--     ID                INTEGER AUTO_INCREMENT PRIMARY KEY,
--     COUNTRY_OF_ORIGIN VARCHAR not null,
--     NAME              VARCHAR not null,
--     GENDER            VARCHAR not null
-- );
INSERT INTO AUTHORS (COUNTRY_OF_ORIGIN, FIRST_NAME, LAST_NAME, GENDER)
VALUES ('USA', 'Герман', 'Мелвилл', 'M'),
       ('UK', 'Joe', 'Abercrombie', 'M'),
       ('USA', 'Джон', 'Браннер', 'M'),
       ('UK', 'Терри', 'Пратчетт', 'M'),
       ('UK', 'Нил', 'Гейман', 'M'),
       ('UK', 'Джон', 'Бойн', 'M');

-- create table BOOK_AUTHORS
-- (
--     BOOK_ID   INTEGER not null,
--     AUTHOR_ID INTEGER not null
-- );
INSERT INTO BOOK_AUTHORS (BOOK_ID, AUTHOR_ID)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 2),
       (5, 4),
       (5, 5),
       (6, 4),
       (7, 6);