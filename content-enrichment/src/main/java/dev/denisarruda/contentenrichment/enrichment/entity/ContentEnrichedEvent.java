package dev.denisarruda.contentenrichment.enrichment.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record ContentEnrichedEvent(
        String contentId,
        String title,
        String description,
        String genre,
        String region,
        String timestamp,
        EnrichmentResult enrichment) {

    public static ContentEnrichedEvent from(ContentPublishedEvent source, EnrichmentResult enrichment) {
        return new ContentEnrichedEvent(
                source.contentId(),
                source.title(),
                source.description(),
                source.genre(),
                source.region(),
                source.timestamp(),
                enrichment);
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("contentId", contentId)
                .add("title", title)
                .add("description", description)
                .add("genre", genre)
                .add("region", region)
                .add("timestamp", timestamp)
                .add("enrichment", enrichment.toJSON())
                .build();
    }
}
