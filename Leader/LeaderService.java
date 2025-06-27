package com.gips.nextapp.Leader;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gips.nextapp.Entity.ProjectEntity;
import com.gips.nextapp.Entity.TUserEntity;
import com.gips.nextapp.Entity.TaskEntity;
import com.gips.nextapp.Notification.NotificationService;
import com.gips.nextapp.Repository.ProjectRepository;
import com.gips.nextapp.Repository.TaskRepository;

/**
 * リーダー向けのサービス。
 * タスク取得、危険タスク抽出、進捗計算、通知送信を行う。
 */
@Service
public class LeaderService {

	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;
	private final NotificationService notificationService;

	/**
	 * コンストラクタで依存オブジェクトを注入する。
	 */
	public LeaderService(TaskRepository taskRepository,
						 ProjectRepository projectRepository,
						 NotificationService notificationService) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.notificationService = notificationService;
	}

	/**
	 * 指定されたプロジェクトのタスク一覧を取得する。
	 * プロジェクトIDが null の場合は全タスクを取得。
	 */
	public List<TaskEntity> getTasksByProjectId(Long projectId) {
		if (projectId == null) {
			// 全体タスク取得（全プロジェクト）
			return taskRepository.findAll()
			                     .stream()
			                     .sorted(Comparator.comparing(TaskEntity::getEndDate))
			                     .collect(Collectors.toList());
		}
		return taskRepository.findByProject_ProjectIdOrderByEndDateAsc(projectId);
	}

	/**
	 * 締切が近い、または遅れているなどの危険なタスクを取得する。
	 */
	public List<TaskEntity> getDangerousTasksByProjectId(Long projectId) {

		// 今日の日付を取得（基準日として使用）
		LocalDate today = LocalDate.now();

		// 指定プロジェクトのタスク一覧を取得し、ストリームで処理
		return getTasksByProjectId(projectId).stream()

				// 各タスクを条件に基づいてフィルタリング
				.filter(task -> {
					// タスクの開始日を取得
					LocalDate start = task.getStartDate();
					// タスクの終了日を取得
					LocalDate end = task.getEndDate();
					// タスクの進捗率（0〜100）を取得
					int progress = task.getProgress();

					// 終了日が未設定（null）のタスクは対象外
					if (end == null)
						return false;

					// 期限を過ぎていて進捗が100未満（未完了）のタスクをチェック
					boolean isOverdue = end.isBefore(today) && progress < 100;

					// 締切の2日前を過ぎていて進捗が100未満のタスクをチェック
					boolean isNearDeadline = end.minusDays(2).isBefore(today) && progress < 100;

					// 開始日から期限までの期間の半分以上が経過し、進捗が30%未満かどうか
					boolean isHalfTimePassedLowProgress = false;
					if (start != null && end.isAfter(start)) {
						// タスク全体の期間（日数）
						long totalDays = java.time.temporal.ChronoUnit.DAYS.between(start, end);
						// 現在までに経過した日数
						long elapsedDays = java.time.temporal.ChronoUnit.DAYS.between(start, today);
						// 進捗が30%未満で、半分以上経過している場合はフラグを立てる
						isHalfTimePassedLowProgress = elapsedDays > totalDays / 2 && progress <= 30;
					}

					// 上記3つのいずれかの条件に該当するタスクを「危険」と判定
					return isOverdue || isNearDeadline || isHalfTimePassedLowProgress;
				})

				// 条件を満たしたタスクをリストにまとめて返す
				.collect(Collectors.toList());
	}

	/**
	 * 指定プロジェクトの平均進捗率を計算する（0〜100）。
	 */
	public int calculateProgressByProjectId(Long projectId) {
		List<TaskEntity> tasks = getTasksByProjectId(projectId);
		if (tasks.isEmpty())
			return 0;

		int total = tasks.stream().mapToInt(TaskEntity::getProgress).sum();
		return total / tasks.size();
	}

	/**
	 * すべてのプロジェクトを取得する。
	 */
	public List<ProjectEntity> getAllProjects() {
		return projectRepository.findAll();
	}

	/**
	 * タスクの担当者に催促メッセージを送信する。
	 *
	 * @param taskId 対象タスクのID
	 * @param senderId リーダーのユーザーID
	 * @param message メッセージ内容
	 */
	public void sendReminderToMember(Long taskId, String senderId, String message) {

		// タスクIDから該当タスクを取得（存在しなければ例外をスロー）
		TaskEntity task = taskRepository.findById(taskId)
				.orElseThrow(() -> new RuntimeException("タスクが見つかりません"));

		// タスクの担当ユーザー（メンバー）を取得
		TUserEntity receiver = task.getUser();

		// 担当者が設定されていない場合は例外をスロー
		if (receiver == null) {
			throw new RuntimeException("受信者が不明です");
		}

		// 通知サービスを使って、指定された内容の催促メッセージをメンバーへ送信
		notificationService.sendNotification(senderId, receiver.getUserId(), message, taskId);
	}
}
