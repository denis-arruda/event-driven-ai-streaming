package dev.denisarruda.sensitivitycompliance.sensitivity.boundary;

import dev.denisarruda.sensitivitycompliance.sensitivity.control.SensitivityOrchestrator;
import dev.denisarruda.sensitivitycompliance.sensitivity.entity.ContentEnrichedEvent;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.json.Json;
import java.io.StringReader;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
class SensitivityComplianceProcessor {

    static final System.Logger LOGGER = System.getLogger(SensitivityComplianceProcessor.class.getName());

    @Inject
    SensitivityOrchestrator orchestrator;

    @Incoming("content-enriched")
    @Outgoing("content-sensitivity-analyzed")
    @ActivateRequestContext
    @RunOnVirtualThread
    String process(String message) {
        var json = Json.createReader(new StringReader(message)).readObject();
        var event = ContentEnrichedEvent.fromJSON(json);
        LOGGER.log(System.Logger.Level.INFO, "Analyzing sensitivity for content: {0}", event.contentId());
        var analyzed = orchestrator.analyze(event);
        LOGGER.log(System.Logger.Level.INFO, "Published sensitivity analysis for content: {0}", event.contentId());
        return analyzed.toJSON().toString();
    }
}
