package com.taskmanager.taskmngr_backend.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {

    private static final String ALGORITHM = "AES";

    public static String encrypt(String value, String secretKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(fitKey(secretKey), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar o token", e);
        }
    }

    public static String decrypt(String encryptedValue, String secretKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(fitKey(secretKey), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getUrlDecoder().decode(encryptedValue);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar o token", e);
        }
    }

    // Ajusta a chave para 16 bytes (AES-128)
    private static byte[] fitKey(String secretKey) {
        byte[] keyBytes = new byte[16];
        byte[] secretBytes = secretKey.getBytes();
        System.arraycopy(secretBytes, 0, keyBytes, 0, Math.min(secretBytes.length, keyBytes.length));
        return keyBytes;
    }
}
