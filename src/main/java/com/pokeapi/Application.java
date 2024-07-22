package com.pokeapi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.http.HttpResponse;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.ui.FlatLineBorder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pokeapi.components.Pokemon;
import com.pokeapi.components.Utilities;

public class Application {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatArcIJTheme());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pokémon Gallery");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            List<JsonObject> pokemons = Pokemon.fetchAllPokemonData();

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

            JScrollPane pokeJScrollPane = new JScrollPane(panel);
            pokeJScrollPane.getVerticalScrollBar().setUnitIncrement(30);

            JPanel sidePanel = new JPanel();
            sidePanel.setPreferredSize(new Dimension(200, 100));
            sidePanel.setBackground(Color.white);
            sidePanel.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16), Color.black, 3, 40));

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pokeJScrollPane, sidePanel);
            splitPane.setOneTouchExpandable(true);
            splitPane.setDividerLocation(500);

            int i = 1;
            for (JsonObject pokemon : pokemons) {
                String name = pokemon.get("name").getAsString();
                String url = pokemon.get("url").getAsString();

                JPanel cardJPanel = new JPanel();
                JLabel nameLabel = new JLabel(Utilities.capitalizeFirstLetter(name));
                JLabel urlLabel = new JLabel(url);
                nameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                urlLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                urlLabel.setPreferredSize(new Dimension(10, 15));

                cardJPanel.setLayout(new BoxLayout(cardJPanel, BoxLayout.Y_AXIS));
                cardJPanel.setPreferredSize(new Dimension(100, 350));
                cardJPanel.setBackground(Color.orange);
                cardJPanel.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16), Color.black, 3, 40));

                cardJPanel.add(Box.createVerticalGlue());
                try {
                    BufferedImage bufferedImage = Utilities.loadImage(
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                                    + i + ".png");

                    JLabel image = Utilities.makeImage(bufferedImage, 150, 200);

                    image.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                    cardJPanel.add(image);
                    cardJPanel.add(nameLabel);
                    cardJPanel.add(urlLabel);

                    cardJPanel.add(Box.createVerticalGlue());

                    int index = i;
                    cardJPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            showPokemonDetailsInSidePanel(sidePanel, url, index);
                        }
                    });

                    panel.add(cardJPanel);

                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            frame.add(splitPane);
            frame.setSize(1000, 1200);
            frame.setVisible(true);
        });
    }

    private static void showPokemonDetailsInSidePanel(JPanel sidePanel, String url, int index) {
        try {
            sidePanel.removeAll();

            HttpResponse<String> response = Utilities.fetch(url);
            StringBuilder abilities = new StringBuilder();
            StringBuilder types = new StringBuilder();

            JsonObject pokemonDetails = Utilities.getObject(response.body());

            BufferedImage bufferedImage = Utilities.loadImage(
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                            + pokemonDetails.get("id").getAsString() + ".png");

            JLabel image = Utilities.makeImage(bufferedImage, 300, 300);

            JLabel nameLabel = new JLabel(Utilities.capitalizeFirstLetter(pokemonDetails.get("name").getAsString()));
            nameLabel.setFont(new Font("Monospaced", Font.BOLD, 40));

            JLabel heightLabel = new JLabel("Height: " + pokemonDetails.get("height").getAsString());
            JLabel weightLabel = new JLabel("Weight: " + pokemonDetails.get("weight").getAsString());

            JsonArray abilitiesArray = pokemonDetails.getAsJsonArray("abilities");
            for (JsonElement abilityElement : abilitiesArray) {
                JsonObject abilityObject = abilityElement.getAsJsonObject().get("ability").getAsJsonObject();
                abilities.append(abilityObject.get("name").getAsString()).append(", ");
            }
            if (abilities.length() > 0) {
                abilities.setLength(abilities.length() - 2);
            }

            JsonArray typesArray = pokemonDetails.getAsJsonArray("types");
            for (JsonElement typesElement : typesArray) {
                JsonObject typeObject = typesElement.getAsJsonObject().get("type").getAsJsonObject();
                types.append(typeObject.get("name").getAsString()).append(", ");
            }
            if (types.length() > 0) {
                types.setLength(types.length() - 2);
            }

            JLabel abilitiesJLabel = new JLabel("Abilities: " + abilities.toString());
            JLabel typesJLabel = new JLabel("Types: " + types.toString());

            JPanel detailsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            detailsPanel.add(image, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            detailsPanel.add(nameLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST;
            detailsPanel.add(typesJLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.WEST;
            detailsPanel.add(abilitiesJLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.WEST;
            detailsPanel.add(heightLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.WEST;
            detailsPanel.add(weightLabel, gbc);

            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
            sidePanel.add(detailsPanel);

            sidePanel.revalidate();
            sidePanel.repaint();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
