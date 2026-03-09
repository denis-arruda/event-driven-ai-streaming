package dev.denisarruda.marketingnarrative.marketing.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record AiFinalizedContentEvent(
        ContentSensitivityAnalyzedEvent source,
        MarketingResult marketing) {

    public static AiFinalizedContentEvent from(ContentSensitivityAnalyzedEvent source, MarketingResult marketing) {
        return new AiFinalizedContentEvent(source, marketing);
    }

    public JsonObject toJSON() {
        var marketingJson = Json.createObjectBuilder()
                .add("headlineGlobal", marketing.headlineGlobal())
                .add("tagline", marketing.tagline())
                .add("shortDescription", marketing.shortDescription())
                .build();

        return Json.createObjectBuilder(source.toJSON())
                .add("marketing", marketingJson)
                .build();
    }
}
