CREATE TABLE tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    task_name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    progress INT CHECK (progress BETWEEN 0 AND 100),
    description TEXT,
    project_id INT NOT NULL,
    user_id VARCHAR(20) NOT NULL,
    status ENUM('未着手', '着手', '進行中', 'レビュー待ち', '完了') NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(project_id),
    FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);