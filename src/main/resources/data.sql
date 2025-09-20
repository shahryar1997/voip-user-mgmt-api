-- Insert a test user for authentication testing
-- Password is "password123" encoded with BCrypt
INSERT INTO voip_users (id,username, password, name, extension)
VALUES (90,'testuser123', '$2a$10$bqTVcGKPRCZBq2XTn.fZUu6sP8H8wP5qtCqr/EE3Vjr3q94DUtwUy', 'Test User', '1001')
ON CONFLICT (username) DO NOTHING;
