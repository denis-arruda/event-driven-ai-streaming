package dev.denisarruda.sensitivitycompliance.sensitivity.entity;

import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContentSensitivityAnalyzedEventTest {

    static final String ENRICHED_SAMPLE = """
            {
              "contentId": "S123",
              "title": "Shadow District",
              "description": "A political thriller set in a dystopian city.",
              "genre": "Thriller",
              "region": "GLOBAL",
              "timestamp": "2026-03-01T10:00:00Z",
              "enrichment": {
                "themes": ["corruption", "moral conflict"],
                "emotionalTone": "dark suspense",
                "audienceProfile": "adults 25-45",
                "keywords": ["political intrigue", "power struggle"]
              }
            }
            """;

    @Test
    void accumulatesSourceFieldsWithSensitivity() {
        var source = ContentEnrichedEvent.fromJSON(Json.createReader(new StringReader(ENRICHED_SAMPLE)).readObject());
        var sensitivity = new SensitivityResult("16+", List.of("DE", "IN"), List.of("political corruption theme"));

        var analyzed = ContentSensitivityAnalyzedEvent.from(source, sensitivity);

        assertThat(analyzed.source().contentId()).isEqualTo("S123");
        assertThat(analyzed.sensitivity().ageRatingSuggested()).isEqualTo("16+");
        assertThat(analyzed.sensitivity().sensitiveRegions()).containsExactly("DE", "IN");
    }

    @Test
    void serialisesToJSONWithAllLayers() {
        var source = ContentEnrichedEvent.fromJSON(Json.createReader(new StringReader(ENRICHED_SAMPLE)).readObject());
        var sensitivity = new SensitivityResult("16+", List.of("DE", "IN"), List.of("political corruption theme"));

        var json = ContentSensitivityAnalyzedEvent.from(source, sensitivity).toJSON();

        assertThat(json.getString("contentId")).isEqualTo("S123");
        assertThat(json.getJsonObject("enrichment").getString("emotionalTone")).isEqualTo("dark suspense");
        assertThat(json.getJsonObject("sensitivity").getString("ageRatingSuggested")).isEqualTo("16+");
        assertThat(json.getJsonObject("sensitivity").getJsonArray("riskFlags").getString(0))
                .isEqualTo("political corruption theme");
    }
}
