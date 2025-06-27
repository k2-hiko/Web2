package com.gips.nextapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gips.nextapp.Entity.LoginEntity;

//Spring Data JPA の JpaRepository を継承したリポジトリインターフェース
public interface LoginRepository extends JpaRepository<LoginEntity, String> {

	// ユーザーIDで LoginEntity を検索するメソッド
	LoginEntity findByUserId(String userId);
}