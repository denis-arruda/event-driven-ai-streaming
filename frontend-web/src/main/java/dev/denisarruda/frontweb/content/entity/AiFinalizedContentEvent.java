package dev.denisarruda.frontweb.content.entity;

import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import java.util.List;

public record AiFinalizedContentEvent(
        String contentId,
        String title,
        String description,
        String genre,
        String region,
        String timestamp,
        JsonObject enrichment,
        JsonObject sensitivity,
        JsonObject marketing) {

    public static AiFinalizedContentEvent fromJSON(JsonObject json) {
        return new AiFinalizedContentEvent(
                json.getString("contentId"),
                json.getString("title"),
                json.getString("description"),
                json.getString("genre"),
                json.getString("region"),
                json.getString("timestamp"),
                json.getJsonObject("enrichment"),
                json.getJsonObject("sensitivity"),
                json.getJsonObject("marketing"));
    }

    // Marketing
    public String headlineGlobal() { return marketing.getString("headlineGlobal", ""); }
    public String tagline() { return marketing.getString("tagline", ""); }
    public String shortDescription() { return marketing.getString("shortDescription", ""); }

    // Enrichment
    public String emotionalTone() { return enrichment.getString("emotionalTone", ""); }
    public String audienceProfile() { return enrichment.getString("audienceProfile", ""); }
    public List<String> themes() { return strings(enrichment, "themes"); }
    public List<String> keywords() { return strings(enrichment, "keywords"); }

    // Sensitivity
    public String ageRatingSuggested() { return sensitivity.getString("ageRatingSuggested", ""); }
    public List<String> sensitiveRegions() { return strings(sensitivity, "sensitiveRegions"); }
    public List<String> riskFlags() { return strings(sensitivity, "riskFlags"); }

    static List<String> strings(JsonObject obj, String key) {
        return obj.getJsonArray(key).getValuesAs(JsonString.class)
                .stream().map(JsonString::getString).toList();
    }
}
