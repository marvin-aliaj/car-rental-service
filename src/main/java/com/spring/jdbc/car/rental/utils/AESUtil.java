package com.spring.jdbc.car.rental.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Service
public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "qwBtKtLq6KUM456zbC5ZlQ==";

    // Encrypt data
    public static String encrypt(String data) throws Exception {
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt data
    public static String decrypt(String encryptedData) throws Exception {
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public static void main(String args[]) throws Exception {
        SecretKey key = new SecretKeySpec("qwBtKtLq6KUM456zbC5ZlQ==".getBytes(StandardCharsets.UTF_8), "AES");


        // Convert the key to a Base64 string (for storage or sharing)
//        String base64Key = AESUtil.encodeKeyToString(key);
//        System.out.println("Generated Base64 Key: " + base64Key);

        // Use the key to encrypt data
        String encrypted = AESUtil.encrypt("1234");
        System.out.println("Encrypted data: " + encrypted);

        // Use the Base64 key string to reconstruct the SecretKey
//        SecretKey reconstructedKey = AESUtil.getKeyFromString(base64Key);

        // Decrypt the data with the reconstructed key
        String decrypted = AESUtil.decrypt(encrypted);
        System.out.println("Decrypted data: " + decrypted);
    }
}
