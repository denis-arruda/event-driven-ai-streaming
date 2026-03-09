package dev.denisarruda.frontweb.content.boundary;

import dev.denisarruda.frontweb.content.control.ContentStore;
import dev.denisarruda.frontweb.content.entity.AiFinalizedContentEvent;
import dev.denisarruda.frontweb.content.entity.ContentPublishedEvent;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Path("/")
@ApplicationScoped
@Produces(MediaType.TEXT_HTML)
public class ContentResource {

    static final System.Logger LOGGER = System.getLogger(ContentResource.class.getName());

    @CheckedTemplate
    static class Templates {
        static native TemplateInstance index(List<AiFinalizedContentEvent> events, String activeView);
    }

    @Inject
    ContentStore store;

    @Inject
    @Channel("content-published")
    Emitter<String> publisher;

    @GET
    public TemplateInstance catalog() {
        return Templates.index(store.all(), "catalog");
    }

    @GET
    @Path("/publish")
    public TemplateInstance publishForm() {
        return Templates.index(store.all(), "publish");
    }

    @POST
    @Path("/content")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response publish(
            @FormParam("title") String title,
            @FormParam("description") String description,
            @FormParam("genre") String genre,
            @FormParam("region") String region) {
        var event = new ContentPublishedEvent(
                UUID.randomUUID().toString(),
                title,
                description,
                genre,
                region,
                Instant.now().toString());
        publisher.send(event.toJSON().toString());
        LOGGER.log(System.Logger.Level.INFO, "Published content: {0}", event.contentId());
        return Response.seeOther(URI.create("/")).build();
    }
}
