package dev.denisarruda.contentenrichment.enrichment.control;

import dev.denisarruda.contentenrichment.enrichment.entity.ContentPublishedEvent;
import dev.denisarruda.contentenrichment.enrichment.entity.EnrichmentResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface ContentEnrichmentAgent {

    @SystemMessage("""
            You are a content enrichment specialist for a streaming platform.
            Analyze the provided content metadata and extract:
            - Narrative themes (list of concise strings)
            - Emotional tone (single descriptive string)
            - Audience profile (single string describing the target demographic)
            - Advanced keywords (list of relevant strings)
            Return structured data only. Do not include any explanation.
            """)
    @UserMessage("Analyze this content and return enrichment data: {it}")
    EnrichmentResult analyze(ContentPublishedEvent content);
}
