package com.gips.nextapp.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * タスク情報を表すエンティティクラス。
 * 
 * <p>
 * 「tasks」テーブルと対応しており、プロジェクト内の各タスクの進捗、開始・終了日、担当者などの情報を保持する。
 * </p>
 */
@Entity
@Table(name = "tasks")
public class TaskEntity {

    /** タスクID（主キー、自動採番） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    /** タスク名（必須） */
    @Column(name = "task_name", nullable = false)
    private String taskName;

    /** タスク開始日（必須） */
    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /** タスク終了日（必須） */
    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /** 進捗率（0〜100％） */
    @Column(name = "progress", nullable = false)
    private int progress = 0;

    /** タスクの説明（任意） */
    @Column(name = "description")
    private String description;

    /** 関連するプロジェクト（外部キー） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    /** 担当ユーザー（外部キー） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private TUserEntity user;

    /** タスクの状態（例：未着手・進行中・完了など） */
    @Column(name = "status", nullable = false)
    private String status;

    // --- Getter / Setter ---

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public TUserEntity getUser() {
        return user;
    }

    public void setUser(TUserEntity user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
