package com.gips.nextapp.Login;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gips.nextapp.Entity.TUserEntity;
import com.gips.nextapp.Repository.TUserRepository;

/**
 * ログイン認証サービスクラス。
 * 
 * ユーザーIDとパスワードを元に、ユーザー情報をデータベースから取得して認証を行う。
 */
@Service
public class LoginService {

	/** ユーザー情報を操作するリポジトリ */
	private final TUserRepository tUserRepository;

	/**
	 * コンストラクタによる依存性注入する。
	 * 
	 * @param tUserRepository ユーザー情報にアクセスするリポジトリ
	 */
	public LoginService(TUserRepository tUserRepository) {
		this.tUserRepository = tUserRepository;
	}

	/**
	 * ユーザーIDとパスワードで認証を行う。
	 * 
	 * @param userId ユーザーID
	 * @param password 入力されたプレーンテキストのパスワード
	 * @return 認証に成功した場合は {@link TUserEntity}、失敗した場合は {@code null}
	 * 
	 * 以下のような流れで動作する：
	 * <ol>
	 *   <li>userId に一致するユーザーをデータベースから検索</li>
	 *   <li>該当ユーザーが存在し、かつパスワードが一致すればそのユーザーを返す</li>
	 *   <li>それ以外の場合は null を返す（認証失敗）</li>
	 * </ol>
	 */
	public TUserEntity authenticate(String userId, String password) {
		Optional<TUserEntity> userOpt = tUserRepository.findById(userId);
		if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
			return userOpt.get();
		}
		return null;
	}
}
