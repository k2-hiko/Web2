package com.gips.nextapp.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * ユーザー情報を表すエンティティクラス。
 * 
 * <p>
 * 「t_user」テーブルに対応し、ログインID、パスワード、氏名、ロール（リーダー/メンバー）を管理します。
 * Spring Security や業務ロジックで使用されるユーザー情報の中心となるクラスです。
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "t_user")
public class TUserEntity {

    /** ユーザーID（主キー、最大20文字） */
    @Id
    @Column(name = "user_id", length = 20)
    private String userId;

    /** ハッシュ化されたパスワード（null不可） */
    @Column(nullable = false)
    private String password;

    /** ユーザー表示名（null不可） */
    @Column(name = "user_name", nullable = false)
    private String name;

    /** ユーザーの役割（leader または member） */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Lob
    @Column(name = "background_image")
    private byte[] backgroundImage;

    @Column(name = "background_image_type")
    private String backgroundImageType;

    public byte[] getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(byte[] backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundImageType() {
        return backgroundImageType;
    }

    public void setBackgroundImageType(String backgroundImageType) {
        this.backgroundImageType = backgroundImageType;
    }


    /**
     * ユーザーの役割を表す列挙型。
     * <ul>
     *     <li><code>leader</code> - リーダー権限を持つユーザー</li>
     *     <li><code>member</code> - メンバー権限を持つユーザー</li>
     * </ul>
     */
    public enum Role {
        leader, member
    }

    /**
     * ユーザーの表示名を返す（getter）。
     * 
     * @return ユーザー名（表示名）
     */
    public String getUserName() {
        return name;
    }
}
