package dev.denisarruda.frontweb.content.control;

import dev.denisarruda.frontweb.content.entity.AiFinalizedContentEvent;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class ContentStore {

    final List<AiFinalizedContentEvent> events = new CopyOnWriteArrayList<>();

    public void add(AiFinalizedContentEvent event) {
        events.add(event);
    }

    public List<AiFinalizedContentEvent> all() {
        return events.stream()
                .sorted((a, b) -> b.timestamp().compareTo(a.timestamp()))
                .toList();
    }
}
