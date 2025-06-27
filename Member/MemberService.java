package com.gips.nextapp.Member;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gips.nextapp.Entity.NotificationEntity;
import com.gips.nextapp.Entity.TUserEntity;
import com.gips.nextapp.Entity.TaskEntity;
import com.gips.nextapp.Repository.NotificationRepository;
import com.gips.nextapp.Repository.TUserRepository;
import com.gips.nextapp.Repository.TaskRepository;

@Service
@Transactional
public class MemberService {

    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;
    private final TUserRepository userRepository;

    public MemberService(TaskRepository taskRepository,
                         NotificationRepository notificationRepository,
                         TUserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<TaskEntity> findTasksByUserId(String loginUserId) {
        return taskRepository.findByUserUserId(loginUserId);
    }

    public List<TaskEntity> findOtherTasks(String loginUserId, Long projectId, String userId) {
        if (projectId != null && userId != null && !userId.isEmpty()) {
            return taskRepository.findOthersTasksByProjectAndUser(loginUserId, projectId, userId);
        } else if (projectId != null) {
            return taskRepository.findOthersTasksByProject(loginUserId, projectId);
        } else if (userId != null && !userId.isEmpty()) {
            return taskRepository.findOthersTasksByUser(loginUserId, userId);
        } else {
            return taskRepository.findOthersTasks(loginUserId);
        }
    }

    public List<String> getStatusList() {
        return List.of("未着手", "着手", "進行中", "レビュー待ち", "完了");
    }

    public int calculateProgress(String status) {
        return switch (status) {
            case "未着手" -> 0;
            case "着手" -> 30;
            case "進行中" -> 60;
            case "レビュー待ち" -> 90;
            case "完了" -> 100;
            default -> 0;
        };
    }

    public Map<Long, String> generateTaskAlerts(List<TaskEntity> tasks) {
        List<Long> taskIds = tasks.stream()
                .map(TaskEntity::getTaskId)
                .collect(Collectors.toList());

        List<NotificationEntity> notifications = notificationRepository.findByTask_TaskIdIn(taskIds);

        return notifications.stream()
                .collect(Collectors.groupingBy(
                        NotificationEntity::getTaskId,
                        Collectors.mapping(NotificationEntity::getMessage, Collectors.joining("\n"))
                ));
    }

    public void updateTaskStatus(Long taskId, String status) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("タスクが見つかりません: " + taskId));

        task.setStatus(status);
        task.setProgress(calculateProgress(status));
        taskRepository.save(task);
    }

    public void updateStatusesFromParams(Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("status_")) {
                Long taskId = Long.valueOf(key.substring(7));
                String status = entry.getValue();
                updateTaskStatus(taskId, status);
            }
        }
    }

    public boolean saveBackgroundImage(String userId, MultipartFile file) {
        if (file == null || file.isEmpty()) return false;

        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return false;
            }

            byte[] bytes = file.getBytes();
            if (bytes.length > 5 * 1024 * 1024) { // 5MB
                return false;
            }

            TUserEntity user = userRepository.findByUserId(userId);
            if (user == null) return false;

            user.setBackgroundImage(bytes);
            user.setBackgroundImageType(contentType);
            userRepository.save(user);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getBackgroundBase64(TUserEntity user) {
        if (user != null && user.getBackgroundImage() != null) {
            return Base64.getEncoder().encodeToString(user.getBackgroundImage());
        }
        return null;
    }
}
