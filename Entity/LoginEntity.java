package com.gips.nextapp.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// エンティティクラス: データベースのテーブルとマッピングされるクラス
@Entity
@Table(name = "m_user") // "m_user" というテーブルにマッピングされる
public class LoginEntity {

	// 主キーのフィールド: データベースの "user_id" 列に対応
	@Id
	@Column(name = "user_id") // データベースの列名を指定
	private String userId;

	// パスワードのフィールド: データベースの "password" 列に対応
	@Column(name = "password") // パスワードの列を明示的に指定
	private String password;

	// デフォルトコンストラクタ
	public LoginEntity() {
	}

	// ユーザーIDとパスワードを設定するコンストラクタ
	public LoginEntity(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	// userId を取得するための getter メソッド
	public String getUserId() {
		return userId;
	}

	// userId を設定するための setter メソッド
	public void setUserId(String userId) {
		this.userId = userId;
	}

	// password を取得するための getter メソッド
	public String getPassword() {
		return password;
	}

	// password を設定するための setter メソッド
	public void setPassword(String password) {
		this.password = password;
	}
}