package dev.denisarruda.sensitivitycompliance.sensitivity.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record ContentSensitivityAnalyzedEvent(
        ContentEnrichedEvent source,
        SensitivityResult sensitivity) {

    public static ContentSensitivityAnalyzedEvent from(ContentEnrichedEvent source, SensitivityResult sensitivity) {
        return new ContentSensitivityAnalyzedEvent(source, sensitivity);
    }

    public JsonObject toJSON() {
        var sensitiveRegionsBuilder = Json.createArrayBuilder();
        sensitivity.sensitiveRegions().forEach(sensitiveRegionsBuilder::add);
        var riskFlagsBuilder = Json.createArrayBuilder();
        sensitivity.riskFlags().forEach(riskFlagsBuilder::add);

        var sensitivityJson = Json.createObjectBuilder()
                .add("ageRatingSuggested", sensitivity.ageRatingSuggested())
                .add("sensitiveRegions", sensitiveRegionsBuilder)
                .add("riskFlags", riskFlagsBuilder)
                .build();

        return Json.createObjectBuilder(source.toJSON())
                .add("sensitivity", sensitivityJson)
                .build();
    }
}
