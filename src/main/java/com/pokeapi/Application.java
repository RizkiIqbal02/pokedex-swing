package com.pokeapi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.ui.FlatLineBorder;
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
            frame.setLocationRelativeTo(null);

            List<JsonObject> pokemons = Pokemon.fetchAllPokemonData();

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
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
                    JLabel image = Utilities.loadImage(
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                                    + i + ".png");
                    image.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                    cardJPanel.add(image);
                    cardJPanel.add(nameLabel);
                    cardJPanel.add(urlLabel);

                    cardJPanel.add(Box.createVerticalGlue());

                    int index = i;
                    cardJPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Pokemon.showPokemonDetails(url, index);
                        }
                    });

                    panel.add(cardJPanel);

                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JScrollPane scrollPane = new JScrollPane(panel);
            frame.add(scrollPane);

            frame.setSize(500, 2340);
            frame.setVisible(true);
        });
    }
}
