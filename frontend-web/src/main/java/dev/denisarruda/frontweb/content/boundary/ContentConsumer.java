package dev.denisarruda.frontweb.content.boundary;

import dev.denisarruda.frontweb.content.control.ContentStore;
import dev.denisarruda.frontweb.content.entity.AiFinalizedContentEvent;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import java.io.StringReader;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
class ContentConsumer {

    static final System.Logger LOGGER = System.getLogger(ContentConsumer.class.getName());

    @Inject
    ContentStore store;

    @Incoming("ai-content-finalized")
    @RunOnVirtualThread
    void consume(String message) {
        var json = Json.createReader(new StringReader(message)).readObject();
        var event = AiFinalizedContentEvent.fromJSON(json);
        store.add(event);
        LOGGER.log(System.Logger.Level.INFO, "Received finalized content: {0}", event.contentId());
    }
}
