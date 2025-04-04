package com.example.food_ordering_mobile_app.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SingleOrListOrIdDeserializer implements JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<String> categories = new ArrayList<>();
        if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            for (JsonElement element : array) {
                categories.add(element.getAsString());
            }
        } else if (json.isJsonPrimitive()) {
            categories.add(json.getAsString());
        }
        return categories;
    }
}

