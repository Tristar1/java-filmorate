DELETE FROM FRIENDS;
DELETE FROM LIKES;
DELETE FROM FILMS_GENRES;
DELETE FROM FILMS;
DELETE FROM GENRES;
DELETE FROM USERS;

INSERT INTO GENRES (ID, NAME)
VALUES 
(1,'Комедия'),
(2,'Драма'),
(3,'Мультфильм'),
(4,'Триллер'),
(5,'Документальный'),
(6,'Боевик');

DELETE FROM RATINGS;

INSERT INTO RATINGS (ID, NAME)
VALUES 
(1,'G'),
(2,'PG'),
(3,'PG-13'),
(4,'R'),
(5,'NC-17');
