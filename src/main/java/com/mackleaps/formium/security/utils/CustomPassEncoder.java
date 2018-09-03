package com.mackleaps.formium.security.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CustomPassEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {

        String encodedPass = null;

        try {
            encodedPass = CustomPassEncoder.generateStrongPasswordHash(charSequence);
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(CustomPassEncoder.class.getSimpleName()).log(Level.SEVERE, null, e);
        } catch (InvalidKeySpecException e) {
            Logger.getLogger(CustomPassEncoder.class.getSimpleName()).log(Level.SEVERE, null, e);
        }

        return encodedPass;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        boolean result = false;

        try {
            result = checkPassword(rawPassword.toString(), encodedPassword);
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(CustomPassEncoder.class.getSimpleName()).log(Level.SEVERE, null, e);
        } catch (InvalidKeySpecException e) {
            Logger.getLogger(CustomPassEncoder.class.getSimpleName()).log(Level.SEVERE, null, e);
        }

        return result;
    }

    private static boolean checkPassword(String suggestPassword, String hashPass) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String[] parts = hashPass.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
        PBEKeySpec spec = new PBEKeySpec(suggestPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashCandidate = skf.generateSecret(spec).getEncoded();
        int diff = hash.length ^ hashCandidate.length;

        for (int i = 0; i < hash.length && i < hashCandidate.length; i++)
            diff |= hash[i] ^ hashCandidate[i];

        return diff == 0;
    }

    private static String generateStrongPasswordHash(CharSequence password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toString().toCharArray();
        byte[] salt = getSalt().getBytes();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();

        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];

        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);

        return bytes;
    }
}
