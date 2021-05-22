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
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

create table USER_ROLE
(
    ROLE    VARCHAR not null,
    USER_ID INTEGER not null,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

create table BOOKS
(
    ID        INTEGER AUTO_INCREMENT PRIMARY KEY,
    TITLE     VARCHAR not null,
    FINISHED  BOOLEAN default false,
    OWNED     BOOLEAN not null,
    BOOK_TYPE VARCHAR not null,
    LOVED     BOOLEAN default false,
    NOTE      VARCHAR
);

create table GENRES
(
    BOOK_ID INTEGER not null,
    GENRE   VARCHAR not null,
    CONSTRAINT book_genres_idx UNIQUE (book_id, genre),
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
);

create table AUTHORS
(
    ID                      INTEGER AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME              VARCHAR not null,
    LAST_NAME               VARCHAR not null,
    GENDER                  VARCHAR not null,
    COUNTRY_OF_ORIGIN       VARCHAR not null
);

create table BOOK_AUTHORS
(
    BOOK_ID   INTEGER not null,
    AUTHOR_ID INTEGER not null,
    CONSTRAINT book_author_idx UNIQUE (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE CASCADE
);
