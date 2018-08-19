package com.aasaanshiksha.application.webclient;

import com.aasaanshiksha.application.model.Event;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class EventWebClient {
    private WebClient webClient;

    public EventWebClient() {
        webClient = WebClient.create("http://localhost:8080/event");
    }


    public Flux<Event> getEvents() {
        return webClient.get().retrieve().bodyToFlux(Event.class);
    }
}
