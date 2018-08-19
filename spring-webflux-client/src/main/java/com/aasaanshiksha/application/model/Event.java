package com.aasaanshiksha.application.model;

public class Event {

    private Long id;

    private String eventType;

    public Event() {
    }

    public Event(Long id, String eventType) {
        this.id = id;
        this.eventType = eventType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
