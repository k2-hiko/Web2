package com.gips.nextapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gips.nextapp.Entity.TUserEntity;

/**
 * ユーザー情報（TUserEntity）に対するデータベース操作を行うリポジトリインタフェース。
 * 
 * <p>Spring Data JPA により、ユーザー情報の検索・取得などの機能を提供します。</p>
 */
public interface TUserRepository extends JpaRepository<TUserEntity, String> {

    /**
     * 指定されたロール（役割）を持つユーザーの一覧を取得します。
     * 
     * @param role ユーザーのロール（例：leader, member）
     * @return 該当ロールを持つユーザーのリスト
     */
    List<TUserEntity> findByRole(TUserEntity.Role role);

    /**
     * ユーザーIDに基づいてユーザー情報を1件取得します。
     * 
     * @param userId ユーザーID（主キー）
     * @return 該当ユーザーのエンティティ（見つからない場合は null）
     */
    TUserEntity findByUserId(String userId);
}
