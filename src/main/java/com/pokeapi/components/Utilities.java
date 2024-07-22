package com.pokeapi.components;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Utilities {
    public static BufferedImage loadImage(String url) throws IOException {
        URL imageUrl = new URL(url);
        BufferedImage bufferedImage = ImageIO.read(imageUrl);

        return bufferedImage;
    }

    public static JLabel makeImage(BufferedImage bufferedImage, int width, int height) {
        Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image));
        return label;
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static HttpResponse<String> fetch(String url) {
        HttpResponse<String> response = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static JsonObject getObject(String res) {
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(res, JsonObject.class);
        return object;
    }
}
