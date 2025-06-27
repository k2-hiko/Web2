package com.gips.nextapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gips.nextapp.Entity.ProjectEntity;

/**
 * プロジェクト情報を管理するためのリポジトリインターフェース。
 * 
 * <p>
 * Spring Data JPA を利用して、プロジェクトデータ（ProjectEntity）の永続化・検索を行います。
 * </p>
 */
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    /**
     * プロジェクト名からプロジェクト情報を取得します。
     * 
     * @param projectName プロジェクト名
     * @return 該当するプロジェクトエンティティ（存在しない場合は null）
     */
    ProjectEntity findByProjectName(String projectName);
}
