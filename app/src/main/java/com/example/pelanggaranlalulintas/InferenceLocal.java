package com.example.pelanggaranlalulintas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class InferenceLocal {

    private static final String API_KEY = "x5K94KdQUtbCnSrYDtRU"; // Your API Key
    private static final String MODEL_ENDPOINT = "pelanggaran-tidak-memakai-helm/4/v"; // Model endpoint

    public static String sendImageToServer(File file) throws IOException {
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File not found or invalid file path");
        }

        String base64Image = encodeImageToBase64(file);
        String uploadURL = "https://detect.roboflow.com/" + MODEL_ENDPOINT + "?api_key=" + API_KEY;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(uploadURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            String jsonPayload = "{\"file\": \"" + base64Image + "\"}";
            connection.getOutputStream().write(jsonPayload.getBytes(StandardCharsets.UTF_8));

            int responseCode = connection.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    Scanner scanner = new Scanner(errorStream).useDelimiter("\\A");
                    String errorMessage = scanner.hasNext() ? scanner.next() : "No error message";
                    System.out.println("Error response: " + errorMessage);
                }
            }


            StringBuilder response = new StringBuilder();
            byte[] buffer = new byte[4096];
            int bytesRead;
            try (InputStream inputStream = connection.getInputStream()) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
                }
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error during image upload: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    private static String encodeImageToBase64(File file) throws IOException {
        byte[] imageBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(imageBytes);
        }
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}

