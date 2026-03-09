package dev.denisarruda.marketingnarrative.marketing.entity;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.json.Json;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

class AiFinalizedContentEventTest {

    static final String SENSITIVITY_SAMPLE = """
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
              },
              "sensitivity": {
                "ageRatingSuggested": "16+",
                "sensitiveRegions": ["DE", "IN"],
                "riskFlags": ["political corruption theme"]
              }
            }
            """;

    @Test
    void accumulatesSourceFieldsWithMarketing() {
        var source = ContentSensitivityAnalyzedEvent.fromJSON(Json.createReader(new StringReader(SENSITIVITY_SAMPLE)).readObject());
        var marketing = new MarketingResult(
                "In a city of shadows, trust is the ultimate risk.",
                "Power hides in the dark.",
                "A suspense thriller about corruption and survival.");

        var finalized = AiFinalizedContentEvent.from(source, marketing);

        assertThat(finalized.source().contentId()).isEqualTo("S123");
        assertThat(finalized.marketing().tagline()).isEqualTo("Power hides in the dark.");
    }

    @Test
    void serialisesToJSONWithAllLayers() {
        var source = ContentSensitivityAnalyzedEvent.fromJSON(Json.createReader(new StringReader(SENSITIVITY_SAMPLE)).readObject());
        var marketing = new MarketingResult(
                "In a city of shadows, trust is the ultimate risk.",
                "Power hides in the dark.",
                "A suspense thriller about corruption and survival.");

        var json = AiFinalizedContentEvent.from(source, marketing).toJSON();

        assertThat(json.getString("contentId")).isEqualTo("S123");
        assertThat(json.getJsonObject("enrichment").getString("emotionalTone")).isEqualTo("dark suspense");
        assertThat(json.getJsonObject("sensitivity").getString("ageRatingSuggested")).isEqualTo("16+");
        assertThat(json.getJsonObject("marketing").getString("tagline")).isEqualTo("Power hides in the dark.");
    }
}
