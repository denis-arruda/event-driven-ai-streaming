package dev.denisarruda.sensitivitycompliance.sensitivity.control;

import dev.denisarruda.sensitivitycompliance.sensitivity.entity.ContentEnrichedEvent;
import dev.denisarruda.sensitivitycompliance.sensitivity.entity.ContentSensitivityAnalyzedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class SensitivityOrchestrator {

    @Inject
    SensitivityAgent agent;

    @Retry(maxRetries = 3, delay = 1, delayUnit = ChronoUnit.SECONDS, jitter = 200, jitterDelayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 10, delayUnit = ChronoUnit.SECONDS)
    @Timeout(value = 60, unit = ChronoUnit.SECONDS)
    public ContentSensitivityAnalyzedEvent analyze(ContentEnrichedEvent event) {
        var result = agent.analyze(event);
        return ContentSensitivityAnalyzedEvent.from(event, result);
    }
}
