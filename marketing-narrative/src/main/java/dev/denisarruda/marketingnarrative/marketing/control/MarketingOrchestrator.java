package dev.denisarruda.marketingnarrative.marketing.control;

import dev.denisarruda.marketingnarrative.marketing.entity.AiFinalizedContentEvent;
import dev.denisarruda.marketingnarrative.marketing.entity.ContentSensitivityAnalyzedEvent;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class MarketingOrchestrator {

    @Inject
    MarketingAgent agent;

    @WithSpan("marketing-narrative.generate")
    @Retry(maxRetries = 3, delay = 1, delayUnit = ChronoUnit.SECONDS, jitter = 200, jitterDelayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 10, delayUnit = ChronoUnit.SECONDS)
    @Timeout(value = 60, unit = ChronoUnit.SECONDS)
    public AiFinalizedContentEvent generate(ContentSensitivityAnalyzedEvent event) {
        Span.current().setAttribute("content.id", event.contentId());
        var result = agent.generate(event);
        return AiFinalizedContentEvent.from(event, result);
    }
}
