DELETE FROM films_genres;
DELETE FROM likes;
DELETE FROM films;
DELETE FROM friendship;
DELETE FROM users;
DELETE FROM rating;
DELETE FROM genre;
INSERT INTO genre (genre_id, name) VALUES (1,'Комедия');
INSERT INTO genre (genre_id, name) VALUES (2,'Драма');
INSERT INTO genre (genre_id, name) VALUES (3,'Мультфильм');
INSERT INTO genre (genre_id, name) VALUES (4,'Триллер');
INSERT INTO genre (genre_id, name) VALUES (5,'Документальный');
INSERT INTO genre (genre_id, name) VALUES (6,'Боевик');

INSERT INTO rating (rating_id,name,description)
    VALUES(1,'G','у фильма нет возрастных ограничений');

INSERT INTO rating (rating_id,name,description)
    VALUES(2,'PG','детям рекомендуется смотреть фильм с родителями');

INSERT INTO rating (rating_id,name,description)
    VALUES(3,'PG-13','детям до 13 лет просмотр не желателен');

INSERT INTO rating (rating_id,name,description)
    VALUES(4,'R','лицам до 17 лет просматривать фильм можно только в присутствии взрослого');

INSERT INTO rating (rating_id,name,description)
    VALUES(5,'NC-17','лицам до 18 лет просмотр запрещён');


