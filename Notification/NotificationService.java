package com.gips.nextapp.Notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gips.nextapp.Entity.NotificationEntity;
import com.gips.nextapp.Entity.TUserEntity;
import com.gips.nextapp.Entity.TaskEntity;
import com.gips.nextapp.Repository.NotificationRepository;
import com.gips.nextapp.Repository.TUserRepository;
import com.gips.nextapp.Repository.TaskRepository;

/**
 * 通知機能を管理するサービスクラス。
 *
 * <p>
 * このクラスは、リーダーからメンバーへの通知の作成・更新、
 * およびメンバーが受け取った通知の取得を担当します。
 * </p>
 */
@Service
public class NotificationService {

	private final NotificationRepository repository;
	private final TUserRepository userRepository;
	private final TaskRepository taskRepository;

	/**
	 * コンストラクタで必要なリポジトリを注入する。
	 *
	 * @param repository         通知リポジトリ
	 * @param userRepository     ユーザーリポジトリ
	 * @param taskRepository     タスクリポジトリ
	 */
	public NotificationService(NotificationRepository repository, TUserRepository userRepository,
			TaskRepository taskRepository) {
		this.repository = repository;
		this.userRepository = userRepository;
		this.taskRepository = taskRepository;
	}

	/**
	 * メンバーに対して通知を送信（または更新）する。
	 *
	 * <p>
	 * すでに該当タスクの通知が存在する場合は上書きし、存在しない場合は新規作成する。
	 * </p>
	 *
	 * @param senderId   通知を送るリーダーのユーザーID
	 * @param receiverId 通知を受け取るメンバーのユーザーID
	 * @param message    通知メッセージ内容
	 * @param taskId     通知に関連するタスクID
	 * @throws RuntimeException 該当するユーザーまたはタスクが見つからない場合
	 */
	public void sendNotification(String senderId, String receiverId, String message, Long taskId) {
		// 送信者と受信者、タスク情報を取得
		TUserEntity sender = userRepository.findById(senderId)
				.orElseThrow(() -> new RuntimeException("Sender not found"));
		TUserEntity receiver = userRepository.findById(receiverId)
				.orElseThrow(() -> new RuntimeException("Receiver not found"));
		TaskEntity task = taskRepository.findById(taskId)
				.orElseThrow(() -> new RuntimeException("Task not found"));

		// 該当タスクの既存通知を取得（なければ新規作成）
		NotificationEntity notification = repository.findTopByTask_TaskId(taskId)
				.orElse(new NotificationEntity());

		// 通知内容をセット
		notification.setSender(sender);
		notification.setReceiver(receiver);
		notification.setMessage(message);
		notification.setSentAt(LocalDateTime.now());
		notification.setReadFlg(false);
		notification.setTask(task);

		// 通知保存（新規または上書き）
		repository.save(notification);
	}

	/**
	 * 指定されたメンバー宛の最新通知を1件取得する。
	 *
	 * @param memberId メンバーのユーザーID
	 * @return 通知エンティティのリスト（最新1件）
	 */
	public List<NotificationEntity> getNotificationsForMember(String memberId) {
		return repository.findTop1ByReceiver_UserIdOrderBySentAtDesc(memberId);
	}
}
