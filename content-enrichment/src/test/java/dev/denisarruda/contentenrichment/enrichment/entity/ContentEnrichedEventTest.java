package dev.denisarruda.contentenrichment.enrichment.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContentEnrichedEventTest {

    @Test
    void accumulatesOriginalFieldsWithEnrichment() {
        var source = new ContentPublishedEvent("S123", "Shadow District",
                "A political thriller.", "Thriller", "GLOBAL", "2026-03-01T10:00:00Z");
        var enrichment = new EnrichmentResult(
                List.of("corruption", "moral conflict"),
                "dark suspense",
                "adults 25-45",
                List.of("political intrigue", "power struggle"));

        var enriched = ContentEnrichedEvent.from(source, enrichment);

        assertThat(enriched.contentId()).isEqualTo("S123");
        assertThat(enriched.enrichment().themes()).containsExactly("corruption", "moral conflict");
    }

    @Test
    void serialisesToJSON() {
        var source = new ContentPublishedEvent("S123", "Shadow District",
                "A political thriller.", "Thriller", "GLOBAL", "2026-03-01T10:00:00Z");
        var enrichment = new EnrichmentResult(
                List.of("corruption"), "dark suspense", "adults 25-45", List.of("power struggle"));

        var json = ContentEnrichedEvent.from(source, enrichment).toJSON();

        assertThat(json.getString("contentId")).isEqualTo("S123");
        assertThat(json.getJsonObject("enrichment").getString("emotionalTone")).isEqualTo("dark suspense");
    }
}
