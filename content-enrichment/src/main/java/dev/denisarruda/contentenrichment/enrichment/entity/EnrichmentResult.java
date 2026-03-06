package dev.denisarruda.contentenrichment.enrichment.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import java.util.List;

public record EnrichmentResult(
        List<String> themes,
        String emotionalTone,
        String audienceProfile,
        List<String> keywords) {

    public JsonObject toJSON() {
        var themesBuilder = Json.createArrayBuilder();
        themes.forEach(themesBuilder::add);
        var keywordsBuilder = Json.createArrayBuilder();
        keywords.forEach(keywordsBuilder::add);
        return Json.createObjectBuilder()
                .add("themes", themesBuilder)
                .add("emotionalTone", emotionalTone)
                .add("audienceProfile", audienceProfile)
                .add("keywords", keywordsBuilder)
                .build();
    }

    public static EnrichmentResult fromJSON(JsonObject json) {
        var themes = json.getJsonArray("themes").getValuesAs(JsonString.class)
                .stream().map(JsonString::getString).toList();
        var keywords = json.getJsonArray("keywords").getValuesAs(JsonString.class)
                .stream().map(JsonString::getString).toList();
        return new EnrichmentResult(
                themes,
                json.getString("emotionalTone"),
                json.getString("audienceProfile"),
                keywords);
    }
}
