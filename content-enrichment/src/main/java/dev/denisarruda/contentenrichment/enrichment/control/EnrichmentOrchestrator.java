package dev.denisarruda.contentenrichment.enrichment.control;

import dev.denisarruda.contentenrichment.enrichment.entity.ContentEnrichedEvent;
import dev.denisarruda.contentenrichment.enrichment.entity.ContentPublishedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class EnrichmentOrchestrator {

    @Inject
    ContentEnrichmentAgent agent;

    @Retry(maxRetries = 3, delay = 1, delayUnit = ChronoUnit.SECONDS, jitter = 200, jitterDelayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 10, delayUnit = ChronoUnit.SECONDS)
    @Timeout(value = 60, unit = ChronoUnit.SECONDS)
    public ContentEnrichedEvent enrich(ContentPublishedEvent event) {
        var result = agent.analyze(event);
        return ContentEnrichedEvent.from(event, result);
    }
}
