INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_Last', '{noop}admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO BOOKS (TITLE, FINISHED)
VALUES ('Moby Dick', true),
       ('Best served cold', true),
       ('Всем стоять на Занзибаре', false),
       ('Герои', false),
       ('Благие знамения', true);

INSERT INTO GENRES (BOOK_ID, GENRE)
VALUES (1, 'CLASSICS'),
       (2, 'FANTASY'),
       (3, 'SF'),
       (4, 'FANTASY'),
       (5, 'FANTASY');

INSERT INTO AUTHORS (COUNTRY_OF_ORIGIN, NAME)
VALUES ('USA', 'Герман Мелвилл'),
       ('UK', 'Joe Abercrombie'),
       ('USA', 'Джон Браннер'),
       ('UK', 'Терри Пратчетт'),
       ('UK', 'Нилл Гейман');

INSERT INTO BOOK_AUTHORS (BOOK_ID, AUTHOR_ID)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 2),
       (5, 4),
       (5, 5);