package com.gips.nextapp.Member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gips.nextapp.Entity.TUserEntity;
import com.gips.nextapp.Entity.TaskEntity;
import com.gips.nextapp.Repository.ProjectRepository;
import com.gips.nextapp.Repository.TUserRepository;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final TUserRepository userRepository;
    private final ProjectRepository projectRepository;

    public MemberController(MemberService memberService,
                            TUserRepository userRepository,
                            ProjectRepository projectRepository) {
        this.memberService = memberService;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/member")
    public String showMemberPage(Model model) {
        return getMemberPage(model, null, null);
    }

    @GetMapping
    public String getMemberPage(Model model,
                                @RequestParam(required = false) Long projectId,
                                @RequestParam(required = false) String userId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth != null ? auth.getPrincipal() : null;
        if (auth == null || !auth.isAuthenticated() ||
            (principal instanceof String && "anonymousUser".equals(principal))) {
            return "redirect:/login";
        }

        String loginUserId = auth.getName();

        try {
            List<TaskEntity> myTasks = memberService.findTasksByUserId(loginUserId);
            List<TaskEntity> otherTasks = memberService.findOtherTasks(loginUserId, projectId, userId);

            Map<Long, String> taskAlerts = memberService.generateTaskAlerts(myTasks);
            taskAlerts.putAll(memberService.generateTaskAlerts(otherTasks));

            List<TUserEntity> allUsers = userRepository.findAll();
            List<TUserEntity> filteredUsers = allUsers.stream()
                    .filter(user -> !user.getUserId().equals(loginUserId))
                    .collect(Collectors.toList());

            TUserEntity loginUser = userRepository.findByUserId(loginUserId);
            String loginUserName = loginUser != null ? loginUser.getUserName() : loginUserId;
            model.addAttribute("loginUserName", loginUserName);

            if (loginUser != null &&
                loginUser.getBackgroundImage() != null &&
                loginUser.getBackgroundImageType() != null) {

                String base64 = memberService.getBackgroundBase64(loginUser);
                model.addAttribute("backgroundBase64", base64);
                model.addAttribute("backgroundImageType", loginUser.getBackgroundImageType());
            }

            model.addAttribute("myTasks", myTasks);
            model.addAttribute("otherTasks", otherTasks);
            model.addAttribute("taskAlerts", taskAlerts);
            model.addAttribute("statusList", memberService.getStatusList());
            model.addAttribute("projects", projectRepository.findAll());
            model.addAttribute("users", filteredUsers);
            model.addAttribute("selectedProjectId", projectId);
            model.addAttribute("selectedUserId", userId);
            model.addAttribute("loginUserId", loginUserId);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "仕事の取得に失敗しました。");
            model.addAttribute("myTasks", List.of());
            model.addAttribute("otherTasks", List.of());
            model.addAttribute("taskAlerts", Map.of());
        }

        return "member";
    }

    @PostMapping
    public String updateMultipleTaskStatus(@RequestParam Map<String, String> params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth != null ? auth.getPrincipal() : null;
        if (auth == null || !auth.isAuthenticated() ||
            (principal instanceof String && "anonymousUser".equals(principal))) {
            return "redirect:/login";
        }

        try {
            memberService.updateStatusesFromParams(params);
        } catch (Exception e) {
            System.err.println("仕事の更新でエラーが発生しました: " + e.getMessage());
        }

        return "redirect:/member";
    }

    @PostMapping("/upload-background")
    public String uploadBackgroundImage(@RequestParam("backgroundImage") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth != null ? auth.getPrincipal() : null;
        if (auth == null || !auth.isAuthenticated() ||
            (principal instanceof String && "anonymousUser".equals(principal))) {
            return "redirect:/login";
        }

        String loginUserId = auth.getName();

        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "ファイルが選択されていません。");
            return "redirect:/member";
        }

        try {
            boolean success = memberService.saveBackgroundImage(loginUserId, file);
            if (success) {
                redirectAttributes.addFlashAttribute("success", "背景画像を更新しました。");
            } else {
                redirectAttributes.addFlashAttribute("error", "画像のアップロードに失敗しました。");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "画像のサイズまたは形式に問題があります。5MB以下の画像を選んでください。");
            System.err.println("アップロードエラー: " + e.getMessage());
        }

        return "redirect:/member";
    }
}
