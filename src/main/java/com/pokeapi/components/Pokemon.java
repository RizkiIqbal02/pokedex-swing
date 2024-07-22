package com.pokeapi.components;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Pokemon {
    public static List<JsonObject> fetchAllPokemonData() {
        List<JsonObject> pokemons = new ArrayList<>();
        try {
            String baseUrl = "https://pokeapi.co/api/v2/pokemon";
            int offset = 0;
            int limit = 30;
            String url = baseUrl + "?offset=" + offset + "&limit=" + limit;

            HttpResponse<String> response = Utilities.fetch(url);

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
}
