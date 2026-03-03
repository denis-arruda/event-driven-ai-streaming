package dev.denisarruda.sensitivitycompliance.sensitivity.entity;

import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class ContentEnrichedEventTest {

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
              }
            }
            """;

    @Test
    void parsedFromJSON() {
        var json = Json.createReader(new StringReader(SAMPLE)).readObject();
        var event = ContentEnrichedEvent.fromJSON(json);

        assertThat(event.contentId()).isEqualTo("S123");
        assertThat(event.title()).isEqualTo("Shadow District");
        assertThat(event.themes()).containsExactly("corruption", "moral conflict");
    }

    @Test
    void serialisesToJSONPreservingEnrichment() {
        var json = Json.createReader(new StringReader(SAMPLE)).readObject();
        var event = ContentEnrichedEvent.fromJSON(json);

        var out = event.toJSON();

        assertThat(out.getString("contentId")).isEqualTo("S123");
        assertThat(out.getJsonObject("enrichment").getString("emotionalTone")).isEqualTo("dark suspense");
    }
}
