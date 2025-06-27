package com.gips.nextapp.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン後、ユーザーのロール（役割）に応じて適切な画面にリダイレクトするコントローラー。
 */
@Controller
public class RedirectController {

    /**
     * ロールに応じてリダイレクト先を切り替える。
     *
     * <p>
     * このエンドポイントは、ログイン成功後にアクセスされる。
     * 認証済みユーザーのロールをチェックし、
     * - リーダーなら `/leader`
     * - メンバーなら `/member`
     * にリダイレクトする。
     * 該当ロールがない場合は `/login?error` に戻す。
     * </p>
     *
     * @return ロールに応じたリダイレクト先のパス
     */
    @GetMapping("/role-redirect")
    public String redirectByRole() {
        // 現在の認証情報を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // デバッグログ（本番では削除推奨）
        System.out.println("Authenticated user: " + auth.getName());
        auth.getAuthorities().forEach(a -> System.out.println("Authority: " + a.getAuthority()));

        // ROLE_leader があれば /leader にリダイレクト
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_leader"))) {
            return "redirect:/leader";
        }
        // ROLE_member があれば /member にリダイレクト
        else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_member"))) {
            return "redirect:/member";
        }
        // それ以外はログイン画面にエラー付きで戻す
        else {
            return "redirect:/login?error";
        }
    }
}
