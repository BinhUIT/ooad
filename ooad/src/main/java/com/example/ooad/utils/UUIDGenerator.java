package com.example.ooad.utils;

import java.nio.charset.Charset;
import java.util.Random;

public class UUIDGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    private static final int STRING_LENGTH=6;
    public static String generateRandomCode() {
        StringBuilder randomString = new StringBuilder(STRING_LENGTH);
        Random random = new Random();

        for (int i = 0; i < STRING_LENGTH; i++) {
            
            int index = random.nextInt(CHARACTERS.length());
            
            randomString.append(CHARACTERS.charAt(index));
        }

        return randomString.toString();
    }
}
