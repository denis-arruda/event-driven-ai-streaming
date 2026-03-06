package dev.denisarruda.marketingnarrative.marketing.boundary;

import dev.denisarruda.marketingnarrative.marketing.control.MarketingOrchestrator;
import dev.denisarruda.marketingnarrative.marketing.entity.ContentSensitivityAnalyzedEvent;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.json.Json;
import java.io.StringReader;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
class MarketingNarrativeProcessor {

    static final System.Logger LOGGER = System.getLogger(MarketingNarrativeProcessor.class.getName());

    @Inject
    MarketingOrchestrator orchestrator;

    @Incoming("content-sensitivity-analyzed")
    @Outgoing("ai-content-finalized")
    @ActivateRequestContext
    @RunOnVirtualThread
    String process(String message) {
        var json = Json.createReader(new StringReader(message)).readObject();
        var event = ContentSensitivityAnalyzedEvent.fromJSON(json);
        LOGGER.log(System.Logger.Level.INFO, "Generating marketing narrative for content: {0}", event.contentId());
        var finalized = orchestrator.generate(event);
        LOGGER.log(System.Logger.Level.INFO, "Published finalized content: {0}", event.contentId());
        return finalized.toJSON().toString();
    }
}
