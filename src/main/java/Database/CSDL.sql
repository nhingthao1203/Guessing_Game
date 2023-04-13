use [GuessingGame]
go
-- Tạo bảng người chơi
CREATE TABLE players (
    username VARCHAR(50) PRIMARY KEY, 
    password VARCHAR(50), 
    highscore INT
);

-- Thêm một người chơi mới
INSERT INTO players (username, password, highscore)
VALUES ('admin', '1', 500);

-- Lấy thông tin tất cả các người chơi
SELECT * FROM players;

-- Cập nhật điểm cao nhất của một người chơi
UPDATE players SET highscore = 750 WHERE username = 'user2';

-- Xóa một người chơi khỏi bảng
DELETE FROM players WHERE username = 'user3';
