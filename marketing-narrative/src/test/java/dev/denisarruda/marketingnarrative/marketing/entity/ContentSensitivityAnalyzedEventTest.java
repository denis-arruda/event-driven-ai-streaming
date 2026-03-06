package dev.denisarruda.marketingnarrative.marketing.entity;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.json.Json;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

class ContentSensitivityAnalyzedEventTest {

    static final String SAMPLE = """
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
    void parsedFromJSON() {
        var json = Json.createReader(new StringReader(SAMPLE)).readObject();
        var event = ContentSensitivityAnalyzedEvent.fromJSON(json);

        assertThat(event.contentId()).isEqualTo("S123");
        assertThat(event.themes()).containsExactly("corruption", "moral conflict");
        assertThat(event.riskFlags()).containsExactly("political corruption theme");
    }

    @Test
    void serialisesToJSONPreservingAllLayers() {
        var json = Json.createReader(new StringReader(SAMPLE)).readObject();
        var out = ContentSensitivityAnalyzedEvent.fromJSON(json).toJSON();

        assertThat(out.getString("contentId")).isEqualTo("S123");
        assertThat(out.getJsonObject("enrichment").getString("emotionalTone")).isEqualTo("dark suspense");
        assertThat(out.getJsonObject("sensitivity").getString("ageRatingSuggested")).isEqualTo("16+");
    }
}
