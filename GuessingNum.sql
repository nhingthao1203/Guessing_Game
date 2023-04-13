use [GuessingGame]
go 
CREATE TABLE Account (
    userName nvarchar(20) NOT NULL,
    passworduser nvarchar(20) NOT NULL,
    maxScore int,
    GhiChu NVARCHAR(MAX),
    CONSTRAINT PK_Account PRIMARY KEY (userName)
);

INSERT INTO Account (userName, passworduser, maxScore, GhiChu)
VALUES 
    ('user1', 'password1', 100, 'Note 1'),
    ('user2', 'password2', 80, 'Note 2'),
    ('user3', 'password3', 95, 'Note 3'),
    ('user4', 'password4', 120, 'Note 4'),
    ('user5', 'password5', 70, 'Note 5'),
    ('user6', 'password6', 85, 'Note 6'),
    ('user7', 'password7', 110, 'Note 7'),
    ('user8', 'password8', 90, 'Note 8'),
    ('user9', 'password9', 75, 'Note 9'),
    ('user10', 'password10', 130, 'Note 10'),
    ('user11', 'password11', 95, 'Note 11'),
    ('user12', 'password12', 80, 'Note 12'),
    ('user13', 'password13', 100, 'Note 13'),
    ('user14', 'password14', 70, 'Note 14'),
    ('user15', 'password15', 120, 'Note 15'),
    ('user16', 'password16', 90, 'Note 16'),
    ('user17', 'password17', 110, 'Note 17'),
    ('user18', 'password18', 85, 'Note 18'),
    ('user19', 'password19', 75, 'Note 19'),
    ('user20', 'password20', 130, 'Note 20');

