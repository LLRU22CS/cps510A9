drop TABLE ACTORS;
drop TABLE FILE_LOCATIONS;
drop TABLE VIDEO_CATEGORIES;
drop TABLE RENTAL_DURATION;
drop TABLE CATEGORIES_R1;
drop TABLE CATEGORIES_R2;
drop TABLE SHOPPING_CART;
drop TABLE WISHLIST;
drop TABLE REVIEW;
drop TABLE USER_LIBRARY_R1;
drop TABLE USER_LIBRARY_R2;
drop TABLE USER_TRANSACTION;
drop TABLE PAYMENT_METHOD;
drop TABLE VIDEO_R1;
drop TABLE VIDEO_R2;
drop TABLE STORE_USER;
drop VIEW TOTAL_POINTS;
drop VIEW ALL_SUCCESSFUL_TRANSACTIONS;
drop VIEW DISCOUNTED_WEEKEND_RENTALS;
drop VIEW GREAT_DEALS_TO_OWN;
drop VIEW CARD_EXPIRED_TRANSACTIONS;


CREATE TABLE STORE_USER (
    userID NUMBER NOT NULL,
    email VARCHAR2(200) NOT NULL UNIQUE,
    username VARCHAR2(200) NOT NULL UNIQUE,
    user_password VARCHAR2(150) NOT NULL,
    first_name VARCHAR2(200) NOT NULL,
    last_name VARCHAR2(200) NOT NULL,
    phone_number NUMBER UNIQUE, 
    date_of_birth DATE NOT NULL,
    PRIMARY KEY (userID)
);

CREATE TABLE VIDEO_R1 (
    videoID NUMBER NOT NULL PRIMARY KEY,
    title VARCHAR2(400) NOT NULL,
    release_year NUMBER(4, 0) NOT NULL,
    director VARCHAR2(500) NOT NULL
);

CREATE TABLE VIDEO_R2 (
    videoID NUMBER NOT NULL PRIMARY KEY,
    video_duration NUMBER(3,0) NOT NULL,
    rating VARCHAR2(12) NOT NULL,
    video_description VARCHAR2(2000),
    purchase_price NUMBER(5,2) 
);

CREATE TABLE PAYMENT_METHOD (
    paymentID NUMBER NOT NULL,
    userID NUMBER REFERENCES STORE_USER(userID) ON DELETE CASCADE,
    card_number NUMBER NOT NULL,
    card_type VARCHAR2(255) NOT NULL,
    card_CVV NUMBER NOT NULL,
    card_expiry_date DATE NOT NULL,
    cardholder_first_name VARCHAR2(255) NOT NULL,
    cardholder_last_name VARCHAR2(255) NOT NULL,
    billing_address_1 VARCHAR2(255) NOT NULL,
    billing_address_2 VARCHAR2(255),
    billing_address_city VARCHAR2(255) NOT NULL,
    billing_address_state VARCHAR2(255) NOT NULL,
    billing_address_postal_code VARCHAR2(255) NOT NULL,
    billing_address_country VARCHAR2(255) NOT NULL,
    PRIMARY KEY (paymentID)
);

CREATE TABLE USER_TRANSACTION (
    transactionID NUMBER NOT NULL,
    userID NUMBER REFERENCES STORE_USER(userID) ON DELETE CASCADE,
    paymentID NUMBER REFERENCES PAYMENT_METHOD(paymentID),
    videoID NUMBER REFERENCES VIDEO(videoID),
    is_successful NUMBER(1) NOT NULL,
    transaction_amount NUMBER(38, 2) DEFAULT 0.00 NOT NULL,
    rent_or_purchase VARCHAR2(32) NOT NULL,
    date_time DATE DEFAULT SYSDATE NOT NULL,
    PRIMARY KEY (transactionID)
);

CREATE TABLE CATEGORIES_R1 (
    categoryID NUMBER NOT NULL PRIMARY KEY,
    category_name VARCHAR2(120) NOT NULL
);

CREATE TABLE CATEGORIES_R2 (
    category_name VARCHAR2(120) NOT NULL PRIMARY KEY,
    description VARCHAR2(1000)
);

CREATE TABLE ACTORS (
    videoID NUMBER REFERENCES VIDEO_R1(videoID) ON DELETE CASCADE,
    actor_last_name VARCHAR2(20),
    actor_first_name VARCHAR(20),
    character_name VARCHAR(30),
    PRIMARY KEY (videoID, actor_last_name)
);

CREATE TABLE FILE_LOCATIONS (
    videoID NUMBER REFERENCES VIDEO_R1(videoID) ON DELETE CASCADE,
    file_type VARCHAR2(20) NOT NULL,
    file_location VARCHAR(50) NOT NULL,
    PRIMARY KEY (videoID, file_type)
);

CREATE TABLE VIDEO_CATEGORIES (
    videoID NUMBER REFERENCES VIDEO_R1(videoID) ON DELETE CASCADE,
    categoryID NUMBER REFERENCES CATEGORIES_R1(categoryID),
    PRIMARY KEY (videoID, categoryID)
);
    
CREATE TABLE RENTAL_DURATION (
    videoID NUMBER REFERENCES VIDEO_R1(videoID) ON DELETE CASCADE,
    rental_duration NUMBER NOT NULL CHECK (rental_duration BETWEEN 1 AND 30),
    rental_price NUMBER(5,2) NOT NULL,
    PRIMARY KEY (videoID, rental_duration)
);

CREATE TABLE SHOPPING_CART(
  userID NUMBER REFERENCES STORE_USER(userID) ON DELETE CASCADE,
  videoID NUMBER REFERENCES VIDEO_R1(videoID) ON DELETE CASCADE,
  total_price NUMBER(5,2) DEFAULT 0.00,
  points NUMBER DEFAULT 0
) ;

CREATE TABLE WISHLIST(
  userID NUMBER REFERENCES STORE_USER(userID) ON DELETE CASCADE,
  videoID NUMBER REFERENCES VIDEO_R1(videoID) ON DELETE CASCADE
);

CREATE TABLE REVIEW(
  userID NUMBER REFERENCES STORE_USER(userID) ON DELETE CASCADE,
  videoID NUMBER REFERENCES VIDEO_R1(videoID) ON DELETE CASCADE,
  review VARCHAR2(1000),
  rating NUMBER NOT NULL CHECK (rating BETWEEN 1 AND 5),
  rtitle VARCHAR2(150) NOT NULL,
  PRIMARY KEY(userID, videoID)
);

CREATE TABLE USER_LIBRARY_R1(
  transactionID NUMBER REFERENCES USER_TRANSACTION(transactionID),
  rent_own VARCHAR2(16) NOT NULL,
  rent_duration NUMBER(2,0) DEFAULT 0,
  purchase_date DATE NOT NULL,
  rent_end_date DATE,
  PRIMARY KEY(transactionID)
);

CREATE TABLE USER_LIBRARY_R2(
  videoID NUMBER REFERENCES VIDEO_R1(videoID) ON DELETE CASCADE,
  userID NUMBER REFERENCES STORE_USER(userID) ON DELETE CASCADE,
  transactionID NUMBER REFERENCES USER_LIBRARY_R1(transactionID) ON DELETE CASCADE
);


INSERT INTO STORE_USER VALUES (
    100, 
    'minecraftpro187@gmail.com', 
    'minecraft pro', 
    'racecar23', 
    'Jared', 
    'Fogle', 
    4169795000, 
    TO_DATE('23/08/1977', 'DD/MM/YYYY')
);
INSERT INTO STORE_USER VALUES (
    101, 
    'asdf1234@hotmail.com', 
    'i have no clue', 
    'racecar24', 
    'Kyrie', 
    'Irving', 
    6475652302, 
    TO_DATE('23/08/1992', 'DD/MM/YYYY')
);
INSERT INTO VIDEO VALUES (
    100, 
    'Saving Private Ryan', 
    169, 
    '14A', 
    1998,
    'Following the Normandy Landings, a group of U.S. soldiers go behind enemy lines to retrieve a paratrooper whose brothers have been killed in action.',
    'Steven Spielberg',
    14.99
);
INSERT INTO VIDEO VALUES (
    101, 
    'Inglorious Basterds', 
    153, 
    '18A', 
    2009,
    'In Nazi-occupied France during World War II, a plan to assassinate Nazi leaders by a group of Jewish U.S. soldiers coincides with a theatre owner''s vengeful plans for the same.',
    'Quentin Tarantino',
    15.99
);
INSERT INTO VIDEO VALUES (
    102, 
    'Pulp Fiction', 
    154, 
    '18A', 
    1994,
    'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.',
    'Quentin Tarantino',
    13.99
);
INSERT INTO VIDEO VALUES (
    103, 
    'The Room', 
    99, 
    '18A', 
    2003,
    'Johnny is a successful bank executive who lives quietly in a San Francisco townhouse with his fiance, Lisa. One day, putting aside any scruple, she seduces Johnny''s best friend, Mark. From there, nothing will be the same again.',
    'Tommy Wiseau',
    7.99
);
INSERT INTO VIDEO VALUES (
    104, 
    'Interstellar', 
    169, 
    'PG', 
    2014,
    'A team of explorers travel through a wormhole in space in an attempt to ensure humanity''s survival.',
    'Christopher Nolan',
    10.99
);
INSERT INTO VIDEO VALUES (
    105, 
    'Inception', 
    148, 
    'PG', 
    2010,
    'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.',
    'Christopher Nolan',
    11.99
);
INSERT INTO VIDEO VALUES (
    106, 
    'Happy Gilmore', 
    96, 
    'PG', 
    1996,
    'A rejected hockey player puts his skills to the golf course to save his grandmother''s house.',
    'Dennis Dugan',
    9.99
);
INSERT INTO VIDEO VALUES (
    107, 
    'The Dark Knight Rises', 
    164, 
    '14A', 
    2012,
    'Eight years after the Joker''s reign of anarchy, Batman, with the help of the enigmatic Catwoman, is forced from his exile to save Gotham City from the brutal guerrilla terrorist Bane.',
    'Christopher Nolan',
    9.99
);
INSERT INTO PAYMENT_METHOD VALUES (100, 100, 5555555555554444, 'MASTERCARD', 123, TO_DATE('09/2022', 'MM/YYYY'), 'Jared', 'Fogle', '69 victoria street', NULL, 'Toronto', 'Ontario', 'A1B2C3', 'Canada');
INSERT INTO PAYMENT_METHOD VALUES (101, 100, 4444444444445555, 'MASTERCARD', 456, TO_DATE('09/2023', 'MM/YYYY'), 'Jared', 'Fogle', '69 victoria street', NULL, 'Toronto', 'Ontario', 'A1B2C3', 'Canada');
INSERT INTO PAYMENT_METHOD VALUES (102, 101, 1234567891234567, 'VISA', 789, TO_DATE('10/2021', 'MM/YYYY'), 'Kyrie', 'Irving', '420 yonge street', NULL, 'Toronto', 'Ontario', 'X7Y8Z9', 'Canada');
INSERT INTO PAYMENT_METHOD VALUES (103, 101, 9876543219876543, 'VISA', 654, TO_DATE('10/2025', 'MM/YYYY'), 'Kyrie', 'Irving', '420 yonge street', NULL, 'Toronto', 'Ontario', 'X7Y8Z9', 'Canada');
INSERT INTO USER_TRANSACTION VALUES (100, 100, 101, 100, 0, 14.99, 'PURCHASE', DEFAULT);
INSERT INTO USER_TRANSACTION VALUES (101, 101, 102, 101, 0, 2.99, 'RENT', DEFAULT);
INSERT INTO USER_TRANSACTION VALUES (102, 100, 100, 100, 1, 14.99, 'PURCHASE', DEFAULT);
INSERT INTO USER_TRANSACTION VALUES (103, 101, 103, 101, 1, 2.99, 'RENT', DEFAULT);
INSERT INTO CATEGORIES VALUES (100, 'Stand-Up', 'Stand-up comedy specials');
INSERT INTO CATEGORIES VALUES (101, 'Comedy', 'Family friendly comedy movies');
INSERT INTO CATEGORIES VALUES (102, 'Adult Comedy', '14A, 18A, and R rated comedy movies');
INSERT INTO CATEGORIES VALUES (103, 'Adventure', 'High-thrill stories');
INSERT INTO CATEGORIES VALUES (104, 'Horror', 'Viewer discretion advised for younger audiences');
INSERT INTO CATEGORIES VALUES (105, 'Documentary', 'Non-fiction programs');
INSERT INTO CATEGORIES VALUES (106, 'Drama', 'Thrilling stories of love and heartbreak');
INSERT INTO ACTORS VALUES (100, 'Hanks', 'Tom', 'Captain Miller');
INSERT INTO ACTORS VALUES (100, 'Daman', 'Matt', 'Private Ryan');
INSERT INTO ACTORS VALUES (101, 'Pitt', 'Brad', 'Lt. Aldo Raine');
INSERT INTO ACTORS VALUES (101, 'Kruger', 'Diane', 'Bridget von Hammersmark');
INSERT INTO ACTORS VALUES (102, 'Travolta', 'John', 'Vincent Vega');
INSERT INTO ACTORS VALUES (102, 'Jackson', 'Samuel L.', 'Jules Winnfield');
INSERT INTO ACTORS VALUES (102, 'Thurman', 'Uma', 'Mia Wallace');
INSERT INTO ACTORS VALUES (102, 'Willis', 'Bruce', 'Butch Coolidge');
INSERT INTO ACTORS VALUES (103, 'Wiseau', 'Tommy', 'Johnny');
INSERT INTO ACTORS VALUES (103, 'Sestero', 'Greg', 'Mark');
INSERT INTO ACTORS VALUES (104, 'McConaughey', 'Matthew', 'Cooper');
INSERT INTO ACTORS VALUES (104, 'Hathaway', 'Anne', 'Brand');
INSERT INTO ACTORS VALUES (104, 'Caine', 'Michael', 'Professor Brand');
INSERT INTO ACTORS VALUES (105, 'DiCaprio', 'Leonardo', 'Cobb');
INSERT INTO ACTORS VALUES (105, 'Gordon-Levitt', 'Joseph', 'Arthur');
INSERT INTO ACTORS VALUES (106, 'Sandler', 'Adam', 'Happy Gilmore');
INSERT INTO ACTORS VALUES (106, 'McDonald', 'Christopher', 'Shooter McGavin');
INSERT INTO ACTORS VALUES (106, 'Bowen', 'Julie', 'Virginia Venit');
INSERT INTO ACTORS VALUES (107, 'Bale', 'Christian', 'Bruce Wayne');
INSERT INTO ACTORS VALUES (107, 'Hathaway', 'Anne', 'Selina');
INSERT INTO ACTORS VALUES (107, 'Gordon-Levitt', 'Joseph', 'Blake');
INSERT INTO ACTORS VALUES (107, 'Caine', 'Michael', 'Alfred');
INSERT INTO FILE_LOCATIONS VALUES (100, 'MOVIE', '/videos/movies/MOVIE_100');
INSERT INTO FILE_LOCATIONS VALUES (100, 'THUMBNAIL', '/videos/images/thumbnails/THUMBNAIL_100');
INSERT INTO FILE_LOCATIONS VALUES (102, 'MOVIE', '/videos/categories/adventure/MOVIE_102');
INSERT INTO FILE_LOCATIONS VALUES (102, 'TRAILER', '/videos/trailers/TRAILER_102');
INSERT INTO FILE_LOCATIONS VALUES (102, 'THUMBNAIL', '/videos/images/thumbnails/THUMBNAIL_102');
INSERT INTO FILE_LOCATIONS VALUES (103, 'MOVIE', '/videos/movies/MOVIE_103');
INSERT INTO FILE_LOCATIONS VALUES (103, 'TRAILER', '/videos/trailers/TRAILER_103');
INSERT INTO FILE_LOCATIONS VALUES (103, 'THUMBNAIL', '/videos/images/thumbnails/THUMBNAIL_103');
INSERT INTO FILE_LOCATIONS VALUES (104, 'MOVIE', '/videos/movies/MOVIE_104');
INSERT INTO FILE_LOCATIONS VALUES (104, 'THUMBNAIL', '/videos/images/thumbnails/THUMBNAIL_104');
INSERT INTO FILE_LOCATIONS VALUES (105, 'MOVIE', '/videos/movies/MOVIE_105');
INSERT INTO FILE_LOCATIONS VALUES (105, 'TRAILER', '/videos/trailers/TRAILER_105');
INSERT INTO FILE_LOCATIONS VALUES (105, 'THUMBNAIL', '/videos/images/thumbnails/THUMBNAIL_105');
INSERT INTO FILE_LOCATIONS VALUES (106, 'MOVIE', '/videos/movies/MOVIE_106');
INSERT INTO FILE_LOCATIONS VALUES (106, 'TRAILER', '/videos/trailers/TRAILER_106');
INSERT INTO FILE_LOCATIONS VALUES (106, 'THUMBNAIL', '/videos/images/thumbnails/THUMBNAIL_106');
INSERT INTO FILE_LOCATIONS VALUES (107, 'MOVIE', '/videos/movies/MOVIE_107');
INSERT INTO FILE_LOCATIONS VALUES (107, 'TRAILER', '/videos/trailers/TRAILER_107');
INSERT INTO FILE_LOCATIONS VALUES (107, 'THUMBNAIL', '/videos/images/thumbnails/THUMBNAIL_107');
INSERT INTO VIDEO_CATEGORIES VALUES (100, 103);
INSERT INTO VIDEO_CATEGORIES VALUES (101, 103);
INSERT INTO VIDEO_CATEGORIES VALUES (102, 103);
INSERT INTO VIDEO_CATEGORIES VALUES (103, 106);
INSERT INTO VIDEO_CATEGORIES VALUES (104, 103);
INSERT INTO VIDEO_CATEGORIES VALUES (104, 106);
INSERT INTO VIDEO_CATEGORIES VALUES (105, 103);
INSERT INTO VIDEO_CATEGORIES VALUES (106, 101);
INSERT INTO VIDEO_CATEGORIES VALUES (107, 103);
INSERT INTO RENTAL_DURATION VALUES (101, 2, 2.99);
INSERT INTO RENTAL_DURATION VALUES (101, 5, 4.99);
INSERT INTO RENTAL_DURATION VALUES (102, 2, 1.99);
INSERT INTO RENTAL_DURATION VALUES (102, 5, 4.99);
INSERT INTO RENTAL_DURATION VALUES (103, 3, 0.99);
INSERT INTO RENTAL_DURATION VALUES (103, 7, 1.99);
INSERT INTO RENTAL_DURATION VALUES (106, 1, 1.99);
INSERT INTO RENTAL_DURATION VALUES (106, 3, 3.99);
INSERT INTO RENTAL_DURATION VALUES (106, 7, 5.99);
INSERT INTO SHOPPING_CART VALUES (100,101,5.99,10);
INSERT INTO SHOPPING_CART VALUES (100,100,12.99,18);
INSERT INTO SHOPPING_CART VALUES (101,100,21.69,30);
INSERT INTO WISHLIST VALUES (100,101);
INSERT INTO WISHLIST VALUES (100,100);
INSERT INTO WISHLIST VALUES (101,100);
INSERT INTO REVIEW VALUES (100,101,'Amazing Movie! Watched it with my family and we all had a great time! A must watch!',5,'Awesome!');
INSERT INTO REVIEW VALUES (101,100,'Very simple and cliche movie. Could have been better.',2,'Overhyped');
INSERT INTO REVIEW VALUES (101,104,'Epic! Good Watch',5,'GREAT');
INSERT INTO REVIEW VALUES (100,104,'It was alright',5,'OK');
INSERT INTO USER_LIBRARY_R1 VALUES (101,'rent',2,DATE'2021-10-06',DATE'2021-10-08');
INSERT INTO USER_LIBRARY_R1 VALUES (100,'own',0,DATE'2021-10-06',NULL);
INSERT INTO USER_LIBRARY_R2 VALUES (100,101,101);
INSERT INTO USER_LIBRARY_R2 VALUES (101,100,100);

-- all video titles
SELECT title FROM VIDEO;

-- actors from Inglorious Basterds
SELECT actor_first_name, actor_last_name
    FROM VIDEO v, ACTORS a
    WHERE v.videoID = a.videoID
    AND v.title = 'Inglorious Basterds';
    
-- category name and video titles of all videos in most populated category
SELECT c.category_name, v.title
    FROM 
        (SELECT categoryID FROM 
                (SELECT COUNT(categoryID) catcount, categoryID
                    FROM VIDEO_CATEGORIES
                    GROUP BY categoryID
                    ORDER BY catcount DESC)
            WHERE ROWNUM = 1) x,
        VIDEO v,
        CATEGORIES c,
        VIDEO_CATEGORIES y
    WHERE c.categoryID = x.categoryID
        AND y.categoryID = x.categoryID
        AND v.videoID = y.videoID;
        
-- repeatedly-featured actors
SELECT movies, actor_first_name, actor_last_name
    FROM (select COUNT(actor_last_name) as movies, actor_first_name, actor_last_name from actors group by actor_first_name, actor_last_name)
    WHERE movies > 1
    GROUP BY actor_first_name, actor_last_name, movies;
    
-- view of cheap purchases that can't be rented
CREATE VIEW GREAT_DEALS_TO_OWN (title, new_low_price) AS
    (SELECT v.title, v.purchase_price
        FROM VIDEO v
        WHERE v.purchase_price < 10
            AND NOT EXISTS 
            (SELECT r.videoID FROM RENTAL_DURATION r WHERE v.videoID = r.videoID)
    )
    ORDER BY v.purchase_price ASC;
SELECT * FROM GREAT_DEALS_TO_OWN;

-- retreive the file location of the trailers for all the applicable movies in the adventure category
SELECT v.title, f.file_location 
    FROM VIDEO v, FILE_LOCATIONS f, VIDEO_CATEGORIES c 
    WHERE v.videoID = c.videoID
    AND c.categoryID = 103
    AND f.videoID = v.videoID
    AND f.file_type = 'TRAILER';
    
-- retreive the videoID of all the movies in the adventure category that have trailers
SELECT v.title
    FROM VIDEO v, VIDEO_CATEGORIES c 
    WHERE v.videoID = c.videoID
    AND c.categoryID = 103
    AND EXISTS (
        SELECT * FROM FILE_LOCATIONS f WHERE f.videoID = v.videoID AND f.file_type = 'TRAILER'
    );

-- the amount of options that a video can be rented for (include 0)
SELECT v.title, COUNT(r.videoID) as rental_options
    FROM VIDEO v LEFT JOIN RENTAL_DURATION r ON v.videoID = r.videoID
    GROUP BY r.videoID, v.title
    ORDER BY v.title;

-- view of short-term rentals
CREATE VIEW DISCOUNTED_WEEKEND_RENTALS (title, nights, slashed_cost) AS
    (SELECT v.title, r.rental_duration, rental_price
        FROM VIDEO v, RENTAL_DURATION r
        WHERE v.videoID = r.videoID
            AND r.rental_duration < 3
            AND r.rental_price < 3
    )
    ORDER BY r.rental_duration ASC, r.rental_price ASC;
SELECT * FROM DISCOUNTED_WEEKEND_RENTALS;

-- average purchase price of every movie
SELECT COUNT(purchase_price) as NUMBER_OF_PURCHASEABLE_MOVIES, AVG(purchase_price) as AVERAGE_PURCHASE_PRICE FROM VIDEO;

-- drama category description and movies
SELECT category_name, description FROM CATEGORIES WHERE categoryID = 106
UNION
SELECT v.title, v.rating FROM VIDEO v, VIDEO_CATEGORIES c WHERE v.videoID = c.videoID AND c.categoryID = 106;

-- movies after certain release year
SELECT title, release_year FROM VIDEO WHERE release_year >= 2000 ORDER BY release_year DESC;

-- view of distinguished directors
SELECT v.director, v.title
    FROM (SELECT COUNT(director) as films_directed, director FROM VIDEO GROUP BY director ORDER BY films_directed DESC) x, VIDEO v
    WHERE x.films_directed > 1
        AND x.director = v.director;

-- Movies in the wishlist of the userID 100
SELECT title
  FROM VIDEO v, WISHLIST w, STORE_USER u
  WHERE v.videoID = w.videoID
  AND w.userID = u.userID
  AND w.userID = 100
  ORDER BY TITLE ASC;

--Creates Table TOTAL_POINTS displaying the total points a user has earned
CREATE VIEW TOTAL_POINTS AS
  (SELECT u.username, SUM(s.points) AS POINTS_EARNED
    FROM SHOPPING_CART s, STORE_USER u
    WHERE s.userID = u.userID
    GROUP BY u.username
  );

--Displays table of users and their total points earned
SELECT * FROM TOTAL_POINTS;

--View of users who have earned 30 or more points
SELECT username, POINTS_EARNED
  FROM TOTAL_POINTS
  WHERE POINTS_EARNED >= 30
  ORDER BY POINTS_EARNED ASC;

--View for users who have earned between 25 to 29 points
SELECT *
FROM TOTAL_POINTS
WHERE POINTS_EARNED BETWEEN 25 AND 29;

-- Displays video titles that have recieved no reviews yet
SELECT DISTINCT v.title
FROM VIDEO v, REVIEW rev
WHERE v.videoID NOT IN 
  (SELECT videoID 
    FROM REVIEW);

-- Displays reviews written for video ID of 101
SELECT u.username, v.title, rev.rtitle, rev.review, rev.rating
  FROM REVIEW rev, VIDEO v, STORE_USER u
  WHERE rev.userID = u.userID
  AND v.videoID = rev.videoID
  AND rev.videoID = 101
  ORDER BY rating ASC;

--Displays users library of ID 100 in descending order
SELECT v.title, ul1.rent_own, ul1.rent_duration, ul1.purchase_date, ul1.rent_end_date
  FROM USER_LIBRARY_R1 ul1, USER_LIBRARY_R2 ul2, VIDEO v, STORE_USER u
  WHERE ul2.userID = u.userID
  AND ul1.transactionID = ul1.transactionID
  AND ul2.userID = 100
  ORDER BY ul1.purchase_date DESC;
  
SELECT * FROM STORE_USER;

SELECT first_name, last_name, email, phone_number, date_of_birth
    FROM STORE_USER
    WHERE date_of_birth < TO_DATE('12/31/2000', 'mm/dd/yyyy');

SELECT paymentID, userID, card_expiry_date 
    FROM PAYMENT_METHOD
    WHERE card_expiry_date > TO_DATE('2022-10-10', 'yyyy-mm-dd');

CREATE VIEW ALL_SUCCESSFUL_TRANSACTIONS AS 
    SELECT u.username, t.date_time, t.transaction_amount, t.is_successful 
    FROM STORE_USER u, USER_TRANSACTION t
    WHERE u.userID = t.userID
    AND t.is_successful = 1
    ORDER BY t.date_time DESC;
    
SELECT * FROM ALL_SUCCESSFUL_TRANSACTIONS;

-- Shows all transactions that have failed due to an expired credit card
CREATE VIEW CARD_EXPIRED_TRANSACTIONS AS
    SELECT t.transactionID, t.userID, t.date_time, t.transaction_amount, t.is_successful, p.card_expiry_date
    FROM USER_TRANSACTION t
    INNER JOIN PAYMENT_METHOD p 
    ON t.paymentID=p.paymentID AND p.card_expiry_date < SYSDATE;
    
SELECT * FROM CARD_EXPIRED_TRANSACTIONS;

-- Shows all users & their info that have a valid credit card
SELECT userID, username, email 
    FROM STORE_USER
    WHERE EXISTS (
        SELECT userID, card_expiry_date
        FROM PAYMENT_METHOD
        WHERE card_expiry_date > SYSDATE
    );