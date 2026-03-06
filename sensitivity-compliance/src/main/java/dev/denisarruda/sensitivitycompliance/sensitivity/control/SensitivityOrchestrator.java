package dev.denisarruda.sensitivitycompliance.sensitivity.control;

import dev.denisarruda.sensitivitycompliance.sensitivity.entity.ContentEnrichedEvent;
import dev.denisarruda.sensitivitycompliance.sensitivity.entity.ContentSensitivityAnalyzedEvent;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.temporal.ChronoUnit;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

@ApplicationScoped
public class SensitivityOrchestrator {

    @Inject
    SensitivityAgent agent;

    @WithSpan("sensitivity-compliance.analyze")
    @Retry(maxRetries = 3, delay = 1, delayUnit = ChronoUnit.SECONDS, jitter = 200, jitterDelayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 10, delayUnit = ChronoUnit.SECONDS)
    @Timeout(value = 60, unit = ChronoUnit.SECONDS)
    public ContentSensitivityAnalyzedEvent analyze(ContentEnrichedEvent event) {
        Span.current().setAttribute("content.id", event.contentId());
        var result = agent.analyze(event);
        return ContentSensitivityAnalyzedEvent.from(event, result);
    }
}
