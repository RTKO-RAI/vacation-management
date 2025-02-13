INSERT INTO projects (name)
VALUES
    ('Project 1'),
    ('Project 2'),
    ('Project 3');

INSERT INTO users (first_name, last_name, password, email, project_id)
VALUES
    ('Arianit', 'Likaj', 'password123', 'arianit.likaj@example.com', 1),
    ('Ahmet', 'Bucko', 'password123', 'ahmet.bucko@example.com', 1),
    ('Vegim', 'Luboteni', 'password123', 'vegim.luboteni@example.com', 2),
    ('Ard', 'Jorganxhiu', 'password123', 'ard.jorganxhiu@example.com', 2),
    ('Arber', 'Aliu', 'password123', 'arber.aliu@example.com', 3);

INSERT INTO vacations (user_id, vacation_year, total_vacation_days, used_vacation_days, start_date)
VALUES
    (1, 2025, 20, 5, '2025-03-01'),
    (2, 2025, 20, 10, '2025-06-15'),
    (3, 2025, 20, 2, '2025-07-10'),
    (4, 2025, 20, 0, '2025-01-20'),
    (5, 2025, 20, 8, '2025-04-05');
