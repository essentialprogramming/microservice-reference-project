package com.config;

import com.util.io.FileInputResource;

import java.io.IOException;

public class SplashMessage {

    private static final String fileName = "splash/banner.txt";

    public static void printSplash() {
        System.out.println(getMessage() + System.lineSeparator());
    }

    private static String getMessage() {
        try (FileInputResource fileInputResource = new FileInputResource("classpath:" + fileName)) {
            return fileInputResource.getText();
        } catch (IOException e) {
            return "";
        }
    }
}
