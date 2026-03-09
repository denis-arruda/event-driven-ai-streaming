package dev.denisarruda.frontweb.content.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ContentPublishedEventTest {

    @Test
    void serialisesToJSON() {
        var event = new ContentPublishedEvent(
                "c-001",
                "Inception",
                "A thief who steals corporate secrets through dream-sharing technology.",
                "Sci-Fi",
                "GLOBAL",
                "2026-03-01T10:00:00Z");

        var json = event.toJSON();

        assertThat(json.getString("contentId")).isEqualTo("c-001");
        assertThat(json.getString("title")).isEqualTo("Inception");
        assertThat(json.getString("genre")).isEqualTo("Sci-Fi");
    }

    @Test
    void serialisesAllFields() {
        var event = new ContentPublishedEvent("id", "title", "desc", "Drama", "DE", "2026-01-01T00:00:00Z");

        var json = event.toJSON();

        assertThat(json.getString("description")).isEqualTo("desc");
        assertThat(json.getString("region")).isEqualTo("DE");
        assertThat(json.getString("timestamp")).isEqualTo("2026-01-01T00:00:00Z");
    }
}
