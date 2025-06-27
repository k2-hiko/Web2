package com.gips.nextapp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gips.nextapp.Entity.NotificationEntity;

/**
 * 通知エンティティ（NotificationEntity）に対するデータアクセスを行うリポジトリインタフェース。
 * 
 * <p>
 * Spring Data JPA を使用し、通知データの取得・削除などの操作を簡潔に実現します。
 * </p>
 */
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    /**
     * 指定されたタスクIDのリストに該当する通知をすべて取得します。
     *
     * @param taskIds タスクIDのリスト
     * @return 該当する通知のリスト
     */
    List<NotificationEntity> findByTask_TaskIdIn(List<Long> taskIds);

    /**
     * 指定されたユーザーIDに対する最新の通知（1件）を取得します。
     *
     * @param userId 通知の受信者ユーザーID
     * @return 最新の通知（1件）のリスト
     */
    List<NotificationEntity> findTop1ByReceiver_UserIdOrderBySentAtDesc(String userId);

    /**
     * 指定されたタスクIDに対する通知のうち、最初の1件を取得します。
     *
     * @param taskId タスクID
     * @return 該当する通知（1件）の Optional
     */
    Optional<NotificationEntity> findTopByTask_TaskId(Long taskId);

    /**
     * 指定されたタスクIDに関連するすべての通知を削除します。
     *
     * @param taskId タスクID
     */
    void deleteByTask_TaskId(Long taskId);
}
