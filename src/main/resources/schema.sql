drop table if exists USERS;
drop table if exists USER_ROLE;
drop table if exists GENRES;
drop table if exists BOOK_AUTHORS;
drop table if exists BOOKS;
drop table if exists AUTHORS;


create table USERS
(
    ID         INTEGER AUTO_INCREMENT PRIMARY KEY,
    EMAIL      VARCHAR not null,
    FIRST_NAME VARCHAR not null,
    LAST_NAME  VARCHAR not null,
    PASSWORD   VARCHAR not null
);

create table USER_ROLE
(
    ROLE    VARCHAR not null,
    USER_ID INTEGER not null
);

create table BOOKS
(
    ID         INTEGER AUTO_INCREMENT PRIMARY KEY,
    TITLE     VARCHAR not null,
    FINISHED  BOOLEAN not null,
    OWNED     BOOLEAN not null,
    BOOK_TYPE VARCHAR not null
);

create table GENRES
(
    BOOK_ID INTEGER not null,
    GENRE   VARCHAR not null
);

create table AUTHORS
(
    ID         INTEGER AUTO_INCREMENT PRIMARY KEY,
    COUNTRY_OF_ORIGIN       VARCHAR not null,
    FIRST_NAME              VARCHAR not null,
    LAST_NAME               VARCHAR,
    GENDER                  VARCHAR not null
);

create table BOOK_AUTHORS
(
    BOOK_ID   INTEGER not null,
    AUTHOR_ID INTEGER not null
);
