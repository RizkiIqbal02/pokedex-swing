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
    public static JLabel loadImage(String source) throws IOException {
        URL url = new URL(source);
        BufferedImage bufferedImage = ImageIO.read(url);

        Image scaledImage = bufferedImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static HttpResponse<String> getResponse(String url) {
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
