INSERT INTO projects (id, name) VALUES
                                    (1, 'Project Alpha'),
                                    (2, 'Project Beta'),
                                    (3, 'Project Gamma');

INSERT INTO users (id, first_name, last_name, password, email, project_id) VALUES
                                                                               (1, 'Arianit', 'Likaj', 'password123', 'arianit.likaj@example.com', 1),
                                                                               (2, 'Ahmet', 'Bucko', 'password123', 'ahmet.bucko@example.com', 1),
                                                                               (3, 'Vegim', 'Luboteni', 'password123', 'vegim.luboteni@example.com', 2),
                                                                               (4, 'Ard', 'Jorganxhiu', 'password123', 'ard.jorganxhiu@example.com', 2),
                                                                               (5, 'Arber', 'Aliu', 'password123', 'arber.aliu@example.com', 3);

INSERT INTO vacations (user_id, vacation_year, total_vacation_days, used_vacation_days) VALUES
                                                                                            (1, 2025, 20, 5),
                                                                                            (2, 2025, 20, 7),
                                                                                            (3, 2025, 20, 10),
                                                                                            (4, 2025, 20, 8),
                                                                                            (5, 2025, 20, 12);
