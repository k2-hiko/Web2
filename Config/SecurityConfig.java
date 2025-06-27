package com.gips.nextapp.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.gips.nextapp.Service.CustomUserDetailsService;

/**
 * Spring Securityの設定クラス。
 * 認証・認可、ログイン・ログアウト処理、セキュリティルールを定義する。
 */
@Configuration
public class SecurityConfig {

	/** ユーザー認証に使用するサービス（DBからユーザー情報を取得） */
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	/** パスワードのハッシュ化・検証に使うエンコーダー（BCryptなど） */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * セキュリティフィルタチェーンの設定を定義。
	 * 認可ルール、ログイン・ログアウト設定などを構成。
	 *
	 * @param http HttpSecurityインスタンス（Spring Securityの設定用）
	 * @return 設定済みのSecurityFilterChain
	 * @throws Exception 設定エラー発生時にスローされる
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// リクエストごとの認可設定
				.authorizeHttpRequests(auth -> auth
						// 静的リソースとログイン画面は誰でもアクセス可能
						.requestMatchers("/css/**", "/js/**", "/images/**", "/sound/**", "/login", "/role-redirect")
						.permitAll()
						// ロール別のアクセス制御を追加
						.requestMatchers("/leader/**").hasRole("leader")
						.requestMatchers("/member/**").hasRole("member")
						// それ以外は認証が必要
						.anyRequest().authenticated())
				// フォームログインの設定
				.formLogin(form -> form
						.loginPage("/login") // ログイン画面のURL
						.loginProcessingUrl("/login") // 認証処理を行うURL
						.usernameParameter("userId") // ユーザーIDのパラメータ名
						.passwordParameter("password") // パスワードのパラメータ名
						.defaultSuccessUrl("/role-redirect", true) // 認証成功時のリダイレクト先
						.failureUrl("/login?error=true") // 認証失敗時の遷移先
						.permitAll() // 全員アクセス可能
				)
				// ログアウト処理の設定
				.logout(logout -> logout
						.logoutUrl("/logout") // ログアウト処理のURL
						.logoutSuccessUrl("/login?clear=true") // ログアウト後の遷移先
						.invalidateHttpSession(true) // セッション破棄
						.deleteCookies("JSESSIONID") // クッキー削除
						.permitAll() // 全員アクセス可能
				)

				// アクセス拒否（403エラー）時の処理設定
				.exceptionHandling(exception -> exception
						// アクセスが拒否された場合に実行されるカスタムハンドラー
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							// 権限がないURLにアクセスした場合はログイン画面にリダイレクト（パラメータ付き）
							response.sendRedirect("/login?forbidden=true");
						}))

				// キャッシュ制御ヘッダの設定
				.headers(headers -> headers.cacheControl(Customizer.withDefaults()));

		return http.build();
	}

	/**
	 * 認証マネージャーの設定。
	 * CustomUserDetailsService と PasswordEncoder を使用してユーザー認証を行う。
	 *
	 * @param http HttpSecurityインスタンス
	 * @return AuthenticationManagerインスタンス
	 * @throws Exception ビルドエラー発生時
	 */
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(customUserDetailsService) // ユーザー情報の取得元
				.passwordEncoder(passwordEncoder) // パスワード検証方法
				.and()
				.build();
	}
}
