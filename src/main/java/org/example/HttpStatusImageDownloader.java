package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpStatusImageDownloader {

    private HttpStatusChecker checker = new HttpStatusChecker();

    public void downloadStatusImage(int code) throws Exception {
        String urlString = checker.getStatusImage(code);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(code + ".jpg")) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("Image downloaded: " + code + ".jpg");
            } catch (IOException e) {
                throw new IOException("Failed to save image: " + e.getMessage(), e);
            }
        } else {
            throw new Exception("Image not found for status code: " + code);
        }
    }

    public static void main(String[] args) {
        HttpStatusImageDownloader downloader = new HttpStatusImageDownloader();
        try {
            downloader.downloadStatusImage(200);
            downloader.downloadStatusImage(404);
            downloader.downloadStatusImage(10000); // This should throw an exception
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

