package model.events;

import simulation.SimulationLogger;

import java.util.List;

public class Event {
    private String description;

    private EventSource eventSource;
    private List<EventTarget> eventTargets;

    /**
     * Creates a new event with a description.
     * @param description   description of the event
     */
//    public Event(String description, EventSource eventSource, List<EventTarget> eventTarget) {

    public Event(String description, EventSource eventSource, List<EventTarget> eventTarget) {
        this.eventSource = eventSource;
        this.eventTargets = eventTarget;
        this.description = description;
        SimulationLogger.logEvent("New event created. Description: "+ description);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public EventSource getEventSource() {
        return eventSource;
    }

    public void setEventSource(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    public List<EventTarget> getEventTarget() {
        return eventTargets;
    }

    public void setEventTarget(List<EventTarget> eventTarget) {
        this.eventTargets = eventTarget;
    }

    public List<EventTarget> getEventTargets() {
        return eventTargets;
    }
}
