package com.gips.nextapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gips.nextapp.Entity.TaskEntity;

/**
 * タスク情報（TaskEntity）に対するデータベース操作を行うリポジトリインタフェース。
 * 
 * <p>Spring Data JPA を使用し、タスクの取得・検索処理を提供します。</p>
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    /**
     * 指定したユーザーが担当しているタスクを全て取得します。
     *
     * @param loginUserId ログイン中のユーザーID
     * @return 該当ユーザーのタスクリスト
     */
    List<TaskEntity> findByUserUserId(String loginUserId);

    /**
     * 指定したユーザー以外が担当している全タスクを、終了日順に降順で取得します。
     *
     * @param loginUserId 除外するユーザーID（ログイン中のユーザー）
     * @return 他ユーザーのタスクリスト
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.user.userId <> :loginUserId ORDER BY t.endDate DESC")
    List<TaskEntity> findOthersTasks(@Param("loginUserId") String loginUserId);

    /**
     * 指定したユーザー以外が担当しており、指定したプロジェクトに属するタスクを取得します（終了日降順）。
     *
     * @param loginUserId ログインユーザーID（除外対象）
     * @param projectId プロジェクトID（絞り込み対象）
     * @return 条件に一致する他ユーザーのタスク一覧
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.user.userId <> :loginUserId AND t.project.projectId = :projectId ORDER BY t.endDate DESC")
    List<TaskEntity> findOthersTasksByProject(
        @Param("loginUserId") String loginUserId,
        @Param("projectId") Long projectId
    );

    /**
     * 指定したユーザー以外で、特定ユーザーが担当しているタスクを取得します（終了日降順）。
     *
     * @param loginUserId ログインユーザーID（除外対象）
     * @param userId 表示対象ユーザーID
     * @return 指定ユーザーの他人タスク一覧
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.user.userId <> :loginUserId AND t.user.userId = :userId ORDER BY t.endDate DESC")
    List<TaskEntity> findOthersTasksByUser(
        @Param("loginUserId") String loginUserId,
        @Param("userId") String userId
    );

    /**
     * 指定したユーザー以外で、指定したプロジェクトかつユーザーが担当しているタスクを取得します（終了日降順）。
     *
     * @param loginUserId ログインユーザーID（除外対象）
     * @param projectId プロジェクトID
     * @param userId 表示対象ユーザーID
     * @return 指定条件に一致する他ユーザーのタスク一覧
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.user.userId <> :loginUserId AND t.project.projectId = :projectId AND t.user.userId = :userId ORDER BY t.endDate DESC")
    List<TaskEntity> findOthersTasksByProjectAndUser(
        @Param("loginUserId") String loginUserId,
        @Param("projectId") Long projectId,
        @Param("userId") String userId
    );

    /**
     * プロジェクトIDに紐づくすべてのタスクを取得します。
     *
     * @param projectId プロジェクトID
     * @return 指定プロジェクトに属するタスクリスト
     */
    List<TaskEntity> findByProject_ProjectId(Long projectId);
 // 終了日が早い順にソートして、指定プロジェクトのタスクを取得
    List<TaskEntity> findByProject_ProjectIdOrderByEndDateAsc(Long projectId);

}
