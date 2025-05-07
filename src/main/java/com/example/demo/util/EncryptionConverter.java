package com.example.demo.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class EncryptionConverter implements AttributeConverter<String, String> {

    private static final String SECRET_KEY = "1234567890123456";
    private static final String ALGORITHM = "AES";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Помилка шифрування " + e.getMessage(), e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException("Помилка розшифрування: " + e.getMessage(), e);
        }
    }
}
