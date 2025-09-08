-- ====================================================
-- Blackjack Game - MySQL Schema Initialization Script
-- ====================================================

-- ------------------------------
-- Table: player
-- ------------------------------
CREATE TABLE IF NOT EXISTS player (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
    games_played INT DEFAULT 0,
    games_won INT DEFAULT 0,
    total_points INT DEFAULT 0
    );

-- ------------------------------
-- Table: game
-- ------------------------------
CREATE TABLE IF NOT EXISTS game (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    player_id BIGINT NOT NULL,
                                    status ENUM('IN_PROGRESS', 'FINISHED') DEFAULT 'IN_PROGRESS',
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NULL,
    FOREIGN KEY (player_id) REFERENCES player(id) ON DELETE CASCADE
    );

-- ------------------------------
-- Table: move
-- ------------------------------
CREATE TABLE IF NOT EXISTS move (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    game_id BIGINT NOT NULL,
                                    move_type ENUM('HIT', 'STAND', 'DOUBLE', 'SPLIT') NOT NULL,
    bet DECIMAL(10,2),
    result ENUM('WIN', 'LOSE', 'DRAW') DEFAULT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (game_id) REFERENCES game(id) ON DELETE CASCADE
    );

-- ------------------------------
-- View: player_ranking
-- ------------------------------
CREATE OR REPLACE VIEW player_ranking AS
SELECT
    id,
    name,
    games_played,
    games_won,
    total_points,
    RANK() OVER (ORDER BY games_won DESC, total_points DESC) AS position
FROM player;
