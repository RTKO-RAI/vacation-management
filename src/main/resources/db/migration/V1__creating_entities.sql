CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE vacations (
                           id BIGSERIAL PRIMARY KEY,
                           user_id BIGINT NOT NULL,
                           vacation_year INT NOT NULL,
                           total_vacation_days INT DEFAULT 20,
                           used_vacation_days INT DEFAULT 0,
                           FOREIGN KEY (user_id) REFERENCES users(id)
);
