package dev.denisarruda.marketingnarrative.marketing.control;

import dev.denisarruda.marketingnarrative.marketing.entity.ContentSensitivityAnalyzedEvent;
import dev.denisarruda.marketingnarrative.marketing.entity.MarketingResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface MarketingAgent {

    @SystemMessage("""
            You are a creative marketing specialist for a global streaming platform.
            Using the content metadata, enrichment analysis, and sensitivity constraints, generate:
            - A global marketing headline (concise, compelling)
            - A punchy tagline (max 8 words)
            - A short promotional description (max two sentences)
            Respect sensitivity constraints — avoid flagged themes in headlines.
            Return structured data only. Do not include any explanation.
            """)
    @UserMessage("Generate marketing assets for this content: {it}")
    MarketingResult generate(ContentSensitivityAnalyzedEvent event);
}
