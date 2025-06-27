package com.gips.nextapp.Schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gips.nextapp.Entity.ProjectEntity;
import com.gips.nextapp.Entity.TUserEntity;
import com.gips.nextapp.Entity.TUserEntity.Role;
import com.gips.nextapp.Entity.TaskEntity;
import com.gips.nextapp.Repository.NotificationRepository;
import com.gips.nextapp.Repository.ProjectRepository;
import com.gips.nextapp.Repository.TUserRepository;
import com.gips.nextapp.Repository.TaskRepository;

/**
 * スケジュール機能のビジネスロジックを担当するサービスクラス。
 * タスクの登録・更新・削除や、メンバー・プロジェクトの取得処理を行う。
 */
@Service
public class ScheduleService {

	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;
	private final TUserRepository tUserRepository;
	private final NotificationRepository notificationRepository;

	/**
	 * コンストラクタによる依存性注入。
	 * 
	 * @param taskRepository         タスクリポジトリ
	 * @param projectRepository      プロジェクトリポジトリ
	 * @param tUserRepository        ユーザーリポジトリ
	 * @param notificationRepository 通知リポジトリ
	 */
	public ScheduleService(TaskRepository taskRepository, ProjectRepository projectRepository,
			TUserRepository tUserRepository, NotificationRepository notificationRepository) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.tUserRepository = tUserRepository;
		this.notificationRepository = notificationRepository;
	}

	/**
	 * 登録済みのすべてのタスクを取得する。
	 * 
	 * @return タスクのリスト
	 */
	public List<TaskEntity> getAllSchedules() {
		// 一覧画面表示用に全タスク取得
		return taskRepository.findAll();
	}

	/**
	 * ロールが「member」のユーザー一覧を取得する。
	 * 
	 * @return メンバーのユーザーリスト
	 */
	public List<TUserEntity> getAllMembers() {
		// ロールが"member"のユーザーのみ取得（リーダーは含めない）
		return tUserRepository.findByRole(Role.member);
	}

	/**
	 * タスクの登録または更新を行う。
	 * 入力されたプロジェクト名が未登録の場合は、プロジェクトを新規作成する。
	 * 
	 * @param task        登録・更新するタスク
	 * @param projectName プロジェクト名
	 * @param userId      担当メンバーのユーザーID
	 */
	public void registerOrUpdateSchedule(TaskEntity task, String projectName, String userId) {

		// プロジェクト名から既存プロジェクトを検索
		ProjectEntity project = projectRepository.findByProjectName(projectName);

		// プロジェクトが存在しなければ新規作成
		if (project == null) {
			project = new ProjectEntity();
			project.setProjectName(projectName);
			// デフォルトで今日から1ヶ月間のプロジェクトとする
			project.setStartDate(LocalDate.now());
			project.setEndDate(LocalDate.now().plusMonths(1));
			project = projectRepository.save(project); // 保存してID取得
		}

		// 担当ユーザーIDからユーザーを取得
		TUserEntity user = tUserRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("メンバーが存在しません"));

		// タスクにユーザーとプロジェクトをセット
		task.setUser(user);
		task.setProject(project);

		// ステータス未設定なら「未着手」をデフォルトで設定
		if (task.getStatus() == null || task.getStatus().isEmpty()) {
			task.setStatus("未着手");
		}

		// タスクを保存（insertまたはupdate）
		taskRepository.save(task);
	}

	/**
	 * タスクを削除する。先に関連する通知を削除してからタスク本体を削除。
	 * 外部キー制約のエラーを避けるため、通知 → タスクの順で削除する必要がある。
	 * 
	 * ※このメソッドはトランザクション処理が必要なため、@Transactional を付加。
	 * 
	 * @param taskId 削除するタスクID
	 */
	@Transactional
	public void deleteSchedule(Long taskId) {
		// 通知がタスクIDに紐づいている場合、先に削除（外部キー制約対応）
		notificationRepository.deleteByTask_TaskId(taskId);

		// タスク本体を削除
		taskRepository.deleteById(taskId);
	}

	/**
	 * タスクIDを指定して該当するタスクを1件取得する。
	 * 
	 * @param taskId タスクID
	 * @return TaskEntity（見つからなければ null）
	 */
	public TaskEntity findById(Long taskId) {
		// Optionalでラップされているが、ここではnull許容で返す
		return taskRepository.findById(taskId).orElse(null);
	}
}
