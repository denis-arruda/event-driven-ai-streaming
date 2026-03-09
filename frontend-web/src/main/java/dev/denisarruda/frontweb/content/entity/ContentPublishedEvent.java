package dev.denisarruda.frontweb.content.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record ContentPublishedEvent(
        String contentId,
        String title,
        String description,
        String genre,
        String region,
        String timestamp) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("contentId", contentId)
                .add("title", title)
                .add("description", description)
                .add("genre", genre)
                .add("region", region)
                .add("timestamp", timestamp)
                .build();
    }
}
