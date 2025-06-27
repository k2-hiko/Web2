package com.gips.nextapp.Leader;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gips.nextapp.Entity.ProjectEntity;
import com.gips.nextapp.Entity.TaskEntity;

/**
 * リーダー向け画面のコントローラ。
 * ダッシュボード表示や催促メッセージの送信を担当。
 */
@Controller
@RequestMapping("/leader")
public class LeaderController {

	private final LeaderService leaderService;

	/**
	 * コンストラクタインジェクションにより LeaderService を注入
	 */
	public LeaderController(LeaderService leaderService) {
		this.leaderService = leaderService;
	}

	/**
	 * /leader にアクセスされたら /leader/dashboard にリダイレクト。
	 */
	@GetMapping
	public String redirectToDashboard() {
		return "redirect:/leader/dashboard";
	}

	/**
	 * ダッシュボードを表示する。
	 *
	 * @param projectId 選択されたプロジェクトID（任意）
	 * @param model     表示データを渡すためのモデル
	 * @return ダッシュボード画面のテンプレート名
	 *
	 * - 認証されていなければログイン画面にリダイレクト
	 * - タスク一覧、危険タスク、進捗率、プロジェクト一覧を取得し、モデルに追加
	 */
	@GetMapping("/dashboard")
	public String showDashboard(@RequestParam(value = "projectId", required = false) Long projectId,
			Model model) {

		// 現在ログインしているユーザーの認証情報を取得
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// 認証されていない、または匿名ユーザーだった場合はログイン画面へリダイレクト
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			return "redirect:/login";
		}

		// ログイン中のユーザーID（リーダー）を取得
		String leaderId = auth.getName();

		// タスク一覧・危険タスク・進捗率・プロジェクト一覧を取得
		List<TaskEntity> allTasks = leaderService.getTasksByProjectId(projectId);
		List<TaskEntity> riskyTasks = leaderService.getDangerousTasksByProjectId(projectId);
		int overallProgress = leaderService.calculateProgressByProjectId(projectId);
		List<ProjectEntity> projects = leaderService.getAllProjects();

		// モデルに情報を追加
		model.addAttribute("leaderId", leaderId);
		model.addAttribute("allTasks", allTasks);
		model.addAttribute("riskyTasks", riskyTasks);
		model.addAttribute("overallProgress", overallProgress);
		model.addAttribute("projects", projects);
		model.addAttribute("selectedProjectId", projectId); // プルダウンの選択状態保持

		// leader-dashboard.html を表示
		return "leader-dashboard";
	}

	/**
	 * メンバーに催促メッセージを送信する。
	 *
	 * @param taskId  対象タスクのID
	 * @param message 送信するメッセージ
	 * @return 送信結果のHTTPレスポンス
	 *
	 * - 認証されていなければ401を返す
	 * - 成功すれば200、失敗すれば500を返す
	 */
	@PostMapping("/remind")
	@ResponseBody
	public ResponseEntity<Void> sendReminder(@RequestParam("taskId") Long taskId,
			@RequestParam("message") String message) {

		// 認証情報を取得
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// 認証が無効な場合、401 Unauthorized を返す
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			return ResponseEntity.status(401).build();
		}

		// 認証済みのユーザーID（送信者）を取得
		String senderId = auth.getName();

		try {
			// リーダーサービス経由で通知送信処理
			leaderService.sendReminderToMember(taskId, senderId, message);
			return ResponseEntity.ok().build(); // 成功：200 OK
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build(); // 失敗：500 Internal Server Error
		}
	}
}
