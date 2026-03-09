package dev.denisarruda.frontweb.content.entity;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.json.Json;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

class AiFinalizedContentEventTest {

    static final String SAMPLE = """
            {
              "contentId": "c-042",
              "title": "Interstellar",
              "description": "A team of explorers travel through a wormhole in space.",
              "genre": "Sci-Fi",
              "region": "GLOBAL",
              "timestamp": "2026-03-01T10:00:00Z",
              "enrichment": {
                "themes": ["survival", "time dilation"],
                "emotionalTone": "awe and dread",
                "audienceProfile": "adults 18-45",
                "keywords": ["space", "wormhole", "love"]
              },
              "sensitivity": {
                "ageRatingSuggested": "12+",
                "sensitiveRegions": [],
                "riskFlags": []
              },
              "marketing": {
                "headlineGlobal": "Beyond the stars, love endures.",
                "tagline": "Mankind was born on Earth. It was never meant to die here.",
                "shortDescription": "An epic journey through space and time."
              }
            }
            """;

    @Test
    void parsedFromJSON() {
        var json = Json.createReader(new StringReader(SAMPLE)).readObject();
        var event = AiFinalizedContentEvent.fromJSON(json);

        assertThat(event.contentId()).isEqualTo("c-042");
        assertThat(event.title()).isEqualTo("Interstellar");
        assertThat(event.genre()).isEqualTo("Sci-Fi");
    }

    @Test
    void exposesMarketingConvenienceMethods() {
        var json = Json.createReader(new StringReader(SAMPLE)).readObject();
        var event = AiFinalizedContentEvent.fromJSON(json);

        assertThat(event.headlineGlobal()).isEqualTo("Beyond the stars, love endures.");
        assertThat(event.tagline()).isEqualTo("Mankind was born on Earth. It was never meant to die here.");
    }
}
