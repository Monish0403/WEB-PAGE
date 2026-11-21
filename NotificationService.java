package com.iot.doorbell;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationService {

    // Replace with your Telegram Bot Token (from @BotFather) and your Chat ID (from @userinfobot)
    private static final String BOT_TOKEN = "YOUR_TELEGRAM_BOT_TOKEN";
    private static final String OWNER_CHAT_ID = "YOUR_CHAT_ID";

    private final HttpClient httpClient;

    public NotificationService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public void sendDoorbellAlert() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a, dd-MM-yyyy"));
        String message = "🔔 Ding Dong! Someone is at your front door.\nTime: " + timestamp;

        try {
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                BOT_TOKEN, OWNER_CHAT_ID, encodedMessage
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("[SUCCESS] Alert sent to owner at " + timestamp);
            } else {
                System.err.println("[ERROR] Failed to send message. HTTP Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception while sending notification: " + e.getMessage());
        }
    }
}
