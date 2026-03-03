package dev.denisarruda.contentenrichment.enrichment.entity;

import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentPublishedEventTest {

    static final String SAMPLE = """
            {
              "contentId": "S123",
              "title": "Shadow District",
              "description": "A political thriller set in a dystopian city.",
              "genre": "Thriller",
              "region": "GLOBAL",
              "timestamp": "2026-03-01T10:00:00Z"
            }
            """;

    @Test
    void parsedFromJSON() {
        var json = Json.createReader(new java.io.StringReader(SAMPLE)).readObject();
        var event = ContentPublishedEvent.fromJSON(json);

        assertThat(event.contentId()).isEqualTo("S123");
        assertThat(event.title()).isEqualTo("Shadow District");
        assertThat(event.genre()).isEqualTo("Thriller");
    }
}
