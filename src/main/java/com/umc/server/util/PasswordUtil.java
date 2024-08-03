package com.umc.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";

    public static String encryptPassword(String password) {
        try {
            // 지정된 알고리즘(SHA-256)을 사용하여 입력 데이터를 해시화
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);

            // 입력으로 받은 비밀번호 문자열을 바이트 배열로 변환하고, update 메서드를 통해 MessageDigest 객체에 전달
            messageDigest.update(password.getBytes());

            // 비밀번호의 해시 값(바이트 배열)을 Base64 인코딩하여 문자열로 변환(2진 to text)
            return Base64.getEncoder().encodeToString(messageDigest.digest());

        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception);
        }
    }
}
