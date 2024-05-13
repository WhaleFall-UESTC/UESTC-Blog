use uestc_blog
CREATE TABLE articles (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          descr VARCHAR(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                          time VARCHAR(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci
);