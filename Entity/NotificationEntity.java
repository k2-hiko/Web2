package com.gips.nextapp.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * 通知情報を表すエンティティクラス。
 * 
 * <p>
 * リーダーからメンバーへの催促通知などのデータを notifications テーブルに保存するためのクラス。
 * </p>
 */
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    /** 通知ID（主キー、自動採番） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    /** 通知の送信者（ユーザー） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id", nullable = false)
    private TUserEntity sender;

    /** 通知の受信者（ユーザー） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id", nullable = false)
    private TUserEntity receiver;

    /** 関連付けられたタスク */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;

    /** 通知メッセージ本文 */
    @Column(name = "message", nullable = false)
    private String message;

    /** 通知の送信日時 */
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    /** 通知の既読フラグ（false = 未読, true = 既読） */
    @Column(name = "read_flg", nullable = false)
    private boolean readFlg = false;

    /** デフォルトコンストラクタ（JPA用） */
    public NotificationEntity() {
    }

    /**
     * 通知エンティティの生成コンストラクタ。
     *
     * @param sender   通知送信者（リーダーなど）
     * @param receiver 通知受信者（メンバーなど）
     * @param task     関連タスク
     * @param message  通知メッセージ
     */
    public NotificationEntity(TUserEntity sender, TUserEntity receiver, TaskEntity task, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.task = task;
        this.message = message;
        this.sentAt = LocalDateTime.now(); // 作成時点の日時
        this.readFlg = false;
    }

    // --- Getter / Setter ---

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public TUserEntity getSender() {
        return sender;
    }

    public void setSender(TUserEntity sender) {
        this.sender = sender;
    }

    public TUserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(TUserEntity receiver) {
        this.receiver = receiver;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isReadFlg() {
        return readFlg;
    }

    public void setReadFlg(boolean readFlg) {
        this.readFlg = readFlg;
    }

    /**
     * 関連するタスクIDを取得する補助メソッド。
     *
     * @return タスクID（taskがnullの場合はnull）
     */
    public Long getTaskId() {
        if (task == null) {
            return null;
        }
        return task.getTaskId();
    }
}
