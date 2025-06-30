CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id VARCHAR(20) NOT NULL,
    receiver_id VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    sent_at DATETIME NOT NULL,
    read_flg BOOLEAN DEFAULT FALSE,
    task_id INT NOT NULL,  -- ★必ずタスクに紐づく
    FOREIGN KEY (sender_id) REFERENCES t_user(user_id),
    FOREIGN KEY (receiver_id) REFERENCES t_user(user_id),
    FOREIGN KEY (task_id) REFERENCES tasks(task_id)
);