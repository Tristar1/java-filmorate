CREATE TABLE IF NOT EXISTS RATINGS (
  ID INTEGER NOT NULL,
  NAME VARCHAR_IGNORECASE NOT NULL,
  CONSTRAINT RATINGS_PK PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS FILMS (
  ID INTEGER NOT NULL,
  NAME VARCHAR_IGNORECASE NOT NULL,
  DESCRIPTION VARCHAR_IGNORECASE NOT NULL,
  RELEASEDATE DATE NOT NULL,
  DURATION VARCHAR_IGNORECASE NOT NULL,
  RATE INTEGER NOT NULL,
  MPA INTEGER NOT NULL,
  CONSTRAINT FILMS_PK PRIMARY KEY (ID),
  CONSTRAINT FILMS_FK FOREIGN KEY (MPA) REFERENCES RATINGS(ID)
);

CREATE TABLE IF NOT EXISTS GENRES (
  ID INTEGER NOT NULL,
  NAME VARCHAR_IGNORECASE NOT NULL,
  CONSTRAINT GENRES_PK PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS FILMS_GENRES (
  FILM_ID INTEGER NOT NULL,
  GENRE_ID INTEGER NOT NULL,
  CONSTRAINT FILMS_GENRES_FK FOREIGN KEY (FILM_ID) REFERENCES FILMS(ID),
  CONSTRAINT FILMS_GENRES_FK_2 FOREIGN KEY (GENRE_ID) REFERENCES GENRES(ID)
);

CREATE TABLE IF NOT EXISTS USERS (
  ID INTEGER NOT NULL,
  EMAIL VARCHAR_IGNORECASE NOT NULL,
  LOGIN VARCHAR_IGNORECASE NOT NULL,
  NAME VARCHAR_IGNORECASE NOT NULL,
  BIRTHDAY DATE NOT NULL,
  CONSTRAINT USERS_PK PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS LIKES (
  FILM_ID INTEGER,
  USER_ID INTEGER,
  CONSTRAINT LIKES_FK FOREIGN KEY (FILM_ID) REFERENCES FILMS(ID),
  CONSTRAINT LIKES_FK_2 FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);

CREATE TABLE IF NOT EXISTS FRIENDS (
  USER_ID INTEGER,
  FRIEND_ID INTEGER,
  CONSTRAINT FRIENDS_FK FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
  CONSTRAINT FRIENDS_FK_2 FOREIGN KEY (FRIEND_ID) REFERENCES USERS(ID)
);


