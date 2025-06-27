package com.gips.nextapp.Service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gips.nextapp.Entity.TUserEntity;
import com.gips.nextapp.Repository.TUserRepository;

/**
 * Spring Security の認証処理で使用される、ユーザー情報の取得サービス。
 *
 * <p>
 * ユーザーID（userId）からデータベースの情報を検索し、
 * Spring Security が使用できる UserDetails オブジェクトに変換して返す。
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TUserRepository TUserRepository;

    /**
     * 指定されたユーザーIDに対応するユーザー情報をロードする。
     * 
     * @param userId ログイン画面で入力されたユーザーID
     * @return UserDetails（Spring Security 用ユーザー情報）
     * @throws UsernameNotFoundException 指定されたユーザーが存在しない場合にスローされる
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername called with userId=" + userId);
        
        // ユーザーIDでデータベース検索
        TUserEntity user = TUserRepository.findByUserId(userId);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + userId);
        }

        // 認証情報を UserDetails として返す（ROLE_ を付けて付与）
        return new User(
            user.getUserId(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
