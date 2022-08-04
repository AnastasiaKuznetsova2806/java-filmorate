create table IF NOT EXISTS USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING(250) not null,
    LOGIN    CHARACTER VARYING(250) not null,
    USER_NAME     CHARACTER VARYING(250) not null,
    BIRTHDAY DATE                   not null,
    constraint """USERS""_PK"
        primary key (USER_ID)
);

create table IF NOT EXISTS  FRIEND_STATUSES
(
    STATUS_ID INTEGER auto_increment,
    STATUS_NAME      CHARACTER VARYING(250),
    constraint FRIEND_STATUSES_PK
        primary key (STATUS_ID)
);

create table if not exists FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    STATUS_ID INTEGER,
    constraint FRIEND_FRIEND_STATUSES_STATUS_ID_FK
        foreign key (STATUS_ID) references FRIEND_STATUSES ON DELETE CASCADE,
    constraint FRIEND_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS ON DELETE CASCADE,
    constraint FRIEND_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS ON DELETE CASCADE
);

create table IF NOT EXISTS  MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(250) not null,
    constraint MPA_PK
        primary key (MPA_ID)
);

create unique index IF NOT EXISTS  MPA_MPA_NAME_UINDEX
    on MPA (MPA_NAME);

create table IF NOT EXISTS  FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME         CHARACTER VARYING(200) not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    MPA_ID    INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA ON DELETE CASCADE
);

create table IF NOT EXISTS  LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint LIKE_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS ON DELETE CASCADE
);

create table IF NOT EXISTS  GENRES
(
    GENRE_ID INTEGER auto_increment,
    GENRE_NAME     CHARACTER VARYING(250) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create unique index IF NOT EXISTS  GENRE_NAME_UINDEX
    on GENRES (GENRE_NAME);

create table IF NOT EXISTS  FILM_GENRES
(
    FILM_ID       INTEGER not null,
    GENRE_ID      INTEGER not null,
    constraint FILM_GENRE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES ON DELETE CASCADE
);

create table IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID   INTEGER auto_increment,
    DIRECTOR_NAME CHARACTER VARYING(250) not null,
    constraint DIRECTORS_PK
        primary key (DIRECTOR_ID)
);

create table IF NOT EXISTS FILM_DIRECTORS
(
    FILM_ID   INTEGER not null,
    DIRECTOR_ID INTEGER not null,
    constraint FILM_DIRECTORS_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint FILM_DIRECTORS_DIRECTORS_DIRECTOR_ID_FK
        foreign key (DIRECTOR_ID) references DIRECTORS ON DELETE CASCADE,
    constraint FILM_DIRECTORS_PK
        primary key (FILM_ID, DIRECTOR_ID)
);