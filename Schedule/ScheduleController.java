package com.gips.nextapp.Schedule;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gips.nextapp.Entity.ProjectEntity;
import com.gips.nextapp.Entity.TaskEntity;

/**
 * スケジュール作成画面に関するリクエストを処理するコントローラクラス。
 * スケジュールの表示、登録、更新、削除を管理する。
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController {

	private final ScheduleService scheduleService;

	/**
	 * コンストラクタ。ScheduleServiceを注入。
	 *
	 * @param scheduleService スケジュール処理を担当するサービス
	 */
	public ScheduleController(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	/**
	 * スケジュール作成画面を表示する。
	 */
	@GetMapping
	public String showSchedulePage(Model model,
			@ModelAttribute("taskForm") TaskEntity taskForm,
			@RequestParam(value = "errorMessage", required = false) String errorMessage,
			@RequestParam(value = "isEditing", required = false) Boolean isEditing,
			@RequestParam(value = "projectName", required = false) String projectName) {

		// 画面表示に必要なデータをserviceから取得
		model.addAttribute("taskList", scheduleService.getAllSchedules());
		model.addAttribute("memberList", scheduleService.getAllMembers());
		
		// 編集モードかどうか（ボタンの表示切り替え用）
		model.addAttribute("isEditing", isEditing != null && isEditing);
		
		// プロジェクト名がnullのときは空文字を設定
		model.addAttribute("projectName", projectName != null ? projectName : "");
		
		// エラーメッセージがある場合は表示
		model.addAttribute("errorMessage", errorMessage);

		return "schedule";
	}

	/**
	 * スケジュールを新規追加または更新する。
	 */
	@PostMapping("/add")
	public String addOrUpdateSchedule(@ModelAttribute("taskForm") TaskEntity task,
			@RequestParam("projectName") String projectName,
			@RequestParam("userId") String userId,
			@RequestParam(value = "isEditing", required = false) Boolean isEditing,
			Model model) {

		try {
			// サービス層でバリデーション＆登録処理を実行
			scheduleService.registerOrUpdateSchedule(task, projectName, userId);
		} catch (IllegalArgumentException e) {
			// バリデーションエラーがある場合、画面に戻してエラーメッセージ表示
			model.addAttribute("taskForm", task);
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("isEditing", isEditing != null && isEditing);
			model.addAttribute("projectName", projectName);
			return showSchedulePage(model, task, e.getMessage(), isEditing, projectName);
		}

		// 正常登録時は一覧画面にリダイレクト（再読み込み防止）
		return "redirect:/schedule";
	}

	/**
	 * 指定されたタスクIDのスケジュールを編集モードで表示する。
	 */
	@PostMapping("/edit")
	public String editSchedule(@RequestParam("taskId") Long taskId, Model model) {
		// タスクIDから対象のTaskEntityを取得
		TaskEntity task = scheduleService.findById(taskId);

		// 紐づくプロジェクト名を取得
		ProjectEntity project = (ProjectEntity) task.getProject();
		String projectName = project.getProjectName();

		// 編集モードとして画面に渡す
		model.addAttribute("taskForm", task);
		model.addAttribute("isEditing", true);
		model.addAttribute("projectName", projectName);
		
		// 編集対象の内容を含めたスケジュール画面を再表示
		return showSchedulePage(model, task, null, true, projectName);
	}

	/**
	 * 指定されたタスクIDのスケジュールを削除する。
	 */
	@PostMapping("/delete")
	public String deleteSchedule(@RequestParam("taskId") Long taskId) {
		// タスク削除を実行
		scheduleService.deleteSchedule(taskId);
		
		// 一覧画面にリダイレクト
		return "redirect:/schedule";
	}
}
