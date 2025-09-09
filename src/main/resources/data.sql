INSERT INTO player (id, name, games_played, games_won, total_points) VALUES
(1, 'John Doe', 10, 5, 120),
(2, 'Jane Smith', 8, 4, 90),
(3, 'Robert Brown', 15, 7, 150),
(4, 'Alice Williams', 5, 2, 45);

INSERT INTO game (id, player_id, status, start_time, end_time) VALUES
(101, 1, 'FINISHED', '2025-09-06 10:00:00', '2025-09-06 10:05:00'),
(102, 2, 'FINISHED', '2025-09-06 10:15:00', '2025-09-06 10:20:00'),
(103, 3, 'FINISHED', '2025-09-06 11:00:00', '2025-09-06 11:10:00'),
(104, 4, 'IN_PROGRESS', '2025-09-06 11:30:00', NULL);

INSERT INTO move (id, game_id, move_type, bet, result, timestamp) VALUES
(1001, 101, 'HIT', 10.00, NULL, '2025-09-06 10:01:00'),
(1002, 101, 'STAND', 10.00, 'WIN', '2025-09-06 10:02:00'),
(1003, 102, 'HIT', 5.00, NULL, '2025-09-06 10:16:00'),
(1004, 102, 'STAND', 5.00, 'LOSE', '2025-09-06 10:17:00'),
(1005, 103, 'HIT', 20.00, NULL, '2025-09-06 11:01:00'),
(1006, 103, 'HIT', 20.00, NULL, '2025-09-06 11:02:00'),
(1007, 103, 'STAND', 20.00, 'WIN', '2025-09-06 11:03:00'),
(1008, 104, 'HIT', 15.00, NULL, '2025-09-06 11:31:00');