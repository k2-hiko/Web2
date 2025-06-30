CREATE TABLE issues (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    task_id INT NOT NULL,
    description TEXT,
    reported_by VARCHAR(20),
    reported_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('open', 'resolved') DEFAULT 'open',
    FOREIGN KEY (task_id) REFERENCES tasks(task_id),
    FOREIGN KEY (reported_by) REFERENCES t_user(user_id)
);