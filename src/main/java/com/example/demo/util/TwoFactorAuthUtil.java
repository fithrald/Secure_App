package com.example.demo.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class TwoFactorAuthUtil {

    public static String generateSecretKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }
    public static boolean verifyCode(String secretKey, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secretKey, code);
    }


    public static String generateQRCode(String otpAuthURL, int width, int height) {

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(pngData);
        } catch (Exception e) {
            throw new RuntimeException("Помилка генерації QR-кода", e);
        }
    }
}
