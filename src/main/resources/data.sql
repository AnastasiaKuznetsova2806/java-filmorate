MERGE INTO GENRES(GENRE_ID, GENRE_NAME)
    values (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO MPA (MPA_ID, MPA_NAME)
    values (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO FRIEND_STATUSES(STATUS_ID, STATUS_NAME)
    values (1, 'неподтверждённая'),
           (2, 'подтверждённая');