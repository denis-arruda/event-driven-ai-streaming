package dev.denisarruda.sensitivitycompliance.sensitivity.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;

import java.util.List;

public record ContentEnrichedEvent(
        String contentId,
        String title,
        String description,
        String genre,
        String region,
        String timestamp,
        JsonObject enrichment) {

    public static ContentEnrichedEvent fromJSON(JsonObject json) {
        return new ContentEnrichedEvent(
                json.getString("contentId"),
                json.getString("title"),
                json.getString("description"),
                json.getString("genre"),
                json.getString("region"),
                json.getString("timestamp"),
                json.getJsonObject("enrichment"));
    }

    public List<String> themes() {
        return enrichment.getJsonArray("themes").getValuesAs(JsonString.class)
                .stream().map(JsonString::getString).toList();
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("contentId", contentId)
                .add("title", title)
                .add("description", description)
                .add("genre", genre)
                .add("region", region)
                .add("timestamp", timestamp)
                .add("enrichment", enrichment)
                .build();
    }
}
