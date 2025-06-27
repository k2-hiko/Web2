package com.gips.nextapp.Schedule;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * スケジュール作成画面のフォームデータを受け取るためのDTOクラス。
 * ユーザー入力に対応する各フィールドを保持する。
 */
public class ScheduleForm {

	/** プロジェクト名（新規または既存） */
	private String projectName;

	/** 担当ユーザーID */
	private String userId;

	/** タスク名 */
	private String taskName;

	/** タスク開始日（yyyy-MM-dd形式） */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	/** タスク終了日（yyyy-MM-dd形式） */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	/** 備考・説明欄 */
	private String description;

	// --- Getter / Setter ---

	/**
	 * プロジェクト名を取得する。
	 *
	 * @return プロジェクト名
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * プロジェクト名を設定する。
	 *
	 * @param projectName プロジェクト名
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * 担当ユーザーのIDを取得する。
	 *
	 * @return ユーザーID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 担当ユーザーのIDを設定する。
	 *
	 * @param userId ユーザーID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * タスク名を取得する。
	 *
	 * @return タスク名
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * タスク名を設定する。
	 *
	 * @param taskName タスク名
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * タスクの開始日を取得する。
	 *
	 * @return 開始日
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * タスクの開始日を設定する。
	 *
	 * @param startDate 開始日
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * タスクの終了日を取得する。
	 *
	 * @return 終了日
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * タスクの終了日を設定する。
	 *
	 * @param endDate 終了日
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * タスクの説明を取得する。
	 *
	 * @return 説明文
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * タスクの説明を設定する。
	 *
	 * @param description 説明文
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
