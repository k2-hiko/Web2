package com.gips.nextapp.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * プロジェクト情報を保持するエンティティクラス。
 * 
 * <p>
 * このクラスは「projects」テーブルと対応しており、プロジェクトのID、名称、
 * 開始日・終了日などの基本情報を管理する。
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "projects")
public class ProjectEntity {

    /** プロジェクトID（主キー、自動採番） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    /** プロジェクト名（null不可） */
    @Column(name = "project_name", nullable = false)
    private String projectName;

    /** プロジェクト開始日（null不可） */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** プロジェクト終了日（null不可） */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
