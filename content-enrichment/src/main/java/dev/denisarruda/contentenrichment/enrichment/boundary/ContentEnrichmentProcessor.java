package dev.denisarruda.contentenrichment.enrichment.boundary;

import dev.denisarruda.contentenrichment.enrichment.control.EnrichmentOrchestrator;
import dev.denisarruda.contentenrichment.enrichment.entity.ContentPublishedEvent;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.json.Json;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.io.StringReader;

@ApplicationScoped
class ContentEnrichmentProcessor {

    static final System.Logger LOGGER = System.getLogger(ContentEnrichmentProcessor.class.getName());

    @Inject
    EnrichmentOrchestrator orchestrator;

    @Incoming("content-published")
    @Outgoing("content-enriched")
    @ActivateRequestContext
    @RunOnVirtualThread
    String process(String message) {
        var json = Json.createReader(new StringReader(message)).readObject();
        var event = ContentPublishedEvent.fromJSON(json);
        LOGGER.log(System.Logger.Level.INFO, "Enriching content: {0}", event.contentId());
        var enriched = orchestrator.enrich(event);
        LOGGER.log(System.Logger.Level.INFO, "Published enriched content: {0}", event.contentId());
        return enriched.toJSON().toString();
    }
}
