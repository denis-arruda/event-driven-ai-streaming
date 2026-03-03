package dev.denisarruda.contentenrichment.enrichment.entity;

import jakarta.json.JsonObject;

public record ContentPublishedEvent(
        String contentId,
        String title,
        String description,
        String genre,
        String region,
        String timestamp) {

    public static ContentPublishedEvent fromJSON(JsonObject json) {
        return new ContentPublishedEvent(
                json.getString("contentId"),
                json.getString("title"),
                json.getString("description"),
                json.getString("genre"),
                json.getString("region"),
                json.getString("timestamp"));
    }
}
