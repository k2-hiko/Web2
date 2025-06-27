package com.gips.nextapp.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        // ユーザーIDと生パスワードの配列
        String[][] users = {
            {"leader02", "pass5678"},
            {"leader03", "pass9012"},
            {"member02", "pass0002"},
            {"member03", "pass0003"},
            {"member04", "pass0004"},
            {"member05", "pass0005"},
            {"member06", "pass0006"},
            {"member07", "pass0007"},
            {"member08", "pass0008"},
            {"member09", "pass0009"},
            {"member10", "pass0010"},
            {"member11", "pass0011"},
            {"member12", "pass0012"},
            {"member13", "pass0013"},
            {"member14", "pass0014"},
            {"member15", "pass0015"}
        };

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        for (String[] user : users) {
            String userId = user[0];
            String rawPassword = user[1];
            String hashedPassword = encoder.encode(rawPassword);
            System.out.printf("('%s', '%s'),\n", userId, hashedPassword);
        }
    }
}
