package dev.denisarruda.sensitivitycompliance.sensitivity.control;

import dev.denisarruda.sensitivitycompliance.sensitivity.entity.ContentEnrichedEvent;
import dev.denisarruda.sensitivitycompliance.sensitivity.entity.SensitivityResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface SensitivityAgent {

    @SystemMessage("""
            You are a content sensitivity and compliance specialist for a global streaming platform.
            Evaluate the provided content metadata and enrichment data to determine:
            - Suggested age rating (e.g. G, PG, 13+, 16+, 18+)
            - Region-specific restrictions using ISO 3166-1 alpha-2 codes
            - Sensitive content risk flags (e.g. political themes, violence, adult content)
            Return structured data only. Do not include any explanation.
            """)
    @UserMessage("Evaluate sensitivity and compliance for this content: {it}")
    SensitivityResult analyze(ContentEnrichedEvent event);
}
