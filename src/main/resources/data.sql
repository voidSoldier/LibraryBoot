
INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_Last', '{noop}admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO BOOKS (TITLE, FINISHED, OWNED, BOOK_TYPE)
VALUES ('Moby Dick', true, true, 'PAPER'),
       ('Best served cold', true, false, 'NONE'),
       ('Всем стоять на Занзибаре', false, true, 'PAPER'),
       ('Герои', false, true, 'PAPER'),
       ('Благие знамения', true, true, 'PAPER'),
       ('Стража! Стража!', true, true, 'PAPER'),
       ('Абсолютист', true, true, 'PAPER');

INSERT INTO GENRES (BOOK_ID, GENRE)
VALUES (1, 'CLASSICS'),
       (2, 'FANTASY'),
       (3, 'SF'),
       (4, 'FANTASY'),
       (5, 'FANTASY'),
       (6, 'FANTASY'),
       (7, 'MODERN_LIT'),
       (7, 'WAR');

INSERT INTO AUTHORS (FIRST_NAME, LAST_NAME, GENDER, COUNTRY_OF_ORIGIN)
VALUES ('Герман', 'Мелвилл', 'M', 'USA'),
       ('Joe', 'Abercrombie', 'M', 'UK'),
       ('Джон', 'Браннер', 'M', 'USA'),
       ('Терри', 'Пратчетт', 'M', 'UK'),
       ('Нил', 'Гейман', 'M', 'UK'),
       ('Джон', 'Бойн', 'M', 'UK');

INSERT INTO BOOK_AUTHORS (BOOK_ID, AUTHOR_ID)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 2),
       (5, 4),
       (5, 5),
       (6, 4),
       (7, 6);