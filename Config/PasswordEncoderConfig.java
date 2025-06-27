package com.gips.nextapp.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * パスワードを安全にハッシュ化するための設定クラス。
 *
 * <p>
 * Spring Security において、ユーザーのパスワードはプレーンテキストではなく、
 * セキュアなハッシュ化方式（ここでは BCrypt）を使用して保存・検証します。
 * </p>
 */
@Configuration
public class PasswordEncoderConfig {
    
    /**
     * BCrypt方式のパスワードエンコーダーを定義。
     * 
     * @return BCryptPasswordEncoderのインスタンス（パスワードのハッシュ化用）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
