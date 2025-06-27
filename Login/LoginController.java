package com.gips.nextapp.Login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ログイン画面コントローラクラス。
 * 
 * Spring Securityでのログイン処理において、ログインエラーやログアウト完了時に
 * 対応するメッセージを表示するための制御を行う。
 * 
 * URL: /login に対応。
 */
@Controller
public class LoginController {

	/**
	 * ログイン画面を表示する。
	 * 
	 * @param error  ログイン失敗時に Spring Security によって付与されるクエリパラメータ
	 * @param logout ログアウト成功時に付与されるクエリパラメータ
	 * @param model  ビューに渡す属性情報を格納するオブジェクト
	 * @return ログイン画面（login.html）を返す
	 * 
	 * 以下のように動作する：
	 * - /login?error が指定された場合、「ユーザIDまたはパスワードが正しくありません。」というエラーメッセージを表示
	 * - /login?logout が指定された場合、「ログアウトしました。」というメッセージを表示
	 */
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, // 認証エラーがあれば取得
			@RequestParam(value = "logout", required = false) String logout, // ログアウト情報があれば取得
			Model model) {

		// エラーがある場合、エラーメッセージを画面に渡す
		if (error != null) {
			model.addAttribute("errorMessage", "ユーザIDまたはパスワードが正しくありません。");
		}

		// ログアウトした場合、ログアウトメッセージを画面に渡す
		if (logout != null) {
			model.addAttribute("logoutMessage", "ログアウトしました。");
		}

		// login.html を表示する（Thymeleafのテンプレート名）
		return "login";
	}
}
