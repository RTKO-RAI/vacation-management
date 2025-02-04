-- Insert data into the users table
INSERT INTO users (id, first_name, last_name, password, email) VALUES
                                                                   (1, 'Arianit', 'Likaj', 'password123', 'arianit.likaj@example.com'),
                                                                   (2, 'Ahmet', 'Bucko', 'password123', 'ahmet.bucko@example.com'),
                                                                   (3, 'Vegim', 'Luboteni', 'password123', 'vegim.luboteni@example.com'),
                                                                   (4, 'Ard', 'Jorganxhiu', 'password123', 'ard.jorganxhiu@example.com'),
                                                                   (5, 'Arber', 'Aliu', 'password123', 'arber.aliu@example.com');

-- Insert data into the vacations table
INSERT INTO vacations (user_id, vacation_year, total_vacation_days, used_vacation_days) VALUES
                                                                                            (1, 2025, 20, 5),
                                                                                            (2, 2025, 20, 7),
                                                                                            (3, 2025, 20, 10),
                                                                                            (4, 2025, 20, 8),
                                                                                            (5, 2025, 20, 12);
