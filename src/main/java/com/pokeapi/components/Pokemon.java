package com.pokeapi.components;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.ui.FlatLineBorder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Pokemon {
    public static List<JsonObject> fetchAllPokemonData() {
        List<JsonObject> pokemons = new ArrayList<>();
        try {
            String baseUrl = "https://pokeapi.co/api/v2/pokemon";
            int offset = 0;
            int limit = 10;
            String url = baseUrl + "?offset=" + offset + "&limit=" + limit;

            HttpResponse<String> response = Utilities.getResponse(url);

            JsonObject jsonObject = Utilities.getObject(response.body());
            JsonArray results = jsonObject.getAsJsonArray("results");

            for (JsonElement result : results) {
                JsonObject pokemonSummary = result.getAsJsonObject();
                pokemons.add(pokemonSummary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemons;
    }

    public static void showPokemonDetails(String url, int numImage) {
        try {
            HttpResponse<String> response = Utilities.getResponse(url);
            StringBuilder abilities = new StringBuilder();

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            mainPanel.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16), Color.black, 1, 40));

            JFrame detailsFrame = new JFrame("Pokémon Details");
            detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailsFrame.setLocationRelativeTo(null);
            detailsFrame.setSize(400, 300);
            detailsFrame.setResizable(false);

            JPanel topPanel = new JPanel(new BorderLayout());
            JLabel image = Utilities.loadImage(
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                            + numImage + ".png");
            image.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JsonObject jsonObject = Utilities.getObject(response.body());
            String id = jsonObject.get("id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String height = jsonObject.get("height").getAsString();
            String weight = jsonObject.get("weight").getAsString();
            JsonArray abilitiesArray = jsonObject.getAsJsonArray("abilities");

            JLabel nameLabel = new JLabel(Utilities.capitalizeFirstLetter(name));
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 30));
            nameLabel.setHorizontalAlignment(JLabel.CENTER);

            topPanel.add(image, BorderLayout.CENTER);

            JPanel abilitiesPanel = new JPanel();
            abilitiesPanel.setLayout(new BoxLayout(abilitiesPanel, BoxLayout.Y_AXIS));
            abilitiesPanel.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16), Color.black, 3, 40));

            for (JsonElement abilityElement : abilitiesArray) {
                JsonObject abilityObject = abilityElement.getAsJsonObject().get("ability").getAsJsonObject();
                abilities.append(abilityObject.get("name").getAsString()).append(", ");
            }
            if (abilities.length() > 0) {
                abilities.setLength(abilities.length() - 2);
            }
            JLabel idLabel = new JLabel("Id: " + id);
            JLabel abilitiesJLabel = new JLabel("Abilities: " + abilities.toString());
            JLabel heightLabel = new JLabel("Height: " + Utilities.capitalizeFirstLetter(height));
            JLabel weightLabel = new JLabel("Weight: " + Utilities.capitalizeFirstLetter(weight));

            abilitiesPanel.add(nameLabel);
            abilitiesPanel.add(idLabel);
            abilitiesPanel.add(abilitiesJLabel);
            abilitiesPanel.add(heightLabel);
            abilitiesPanel.add(weightLabel);

            mainPanel.add(topPanel);
            mainPanel.add(abilitiesPanel);
            detailsFrame.add(mainPanel);
            detailsFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
