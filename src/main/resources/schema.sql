create table USERS
(
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
    COUNTRY_OF_ORIGIN VARCHAR not null,
    NAME              VARCHAR not null,
    GENDER            VARCHAR not null
);

create table BOOK_AUTHORS
(
    BOOK_ID   INTEGER not null,
    AUTHOR_ID INTEGER not null
);
