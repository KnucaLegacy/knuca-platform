package com.theopus.upload.handling;

import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.service.UploadEventService;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

public class EventLauncher {

    private final ExecutorService service;
    private final EventHandler handler;
    private final UploadEventService eventService;

    public EventLauncher(ExecutorService service, EventHandler handler, UploadEventService eventService) {
        this.service = service;
        this.handler = handler;
        this.eventService = eventService;
    }

    public void submit(UploadEvent event) {
        eventService.updateStatus(event, UploadEvent.Status.SUBMITTED);
        service.submit(() -> {
            try {
                eventService.updateStatus(event, UploadEvent.Status.IN_PROCESS);
                event.setLastProcessing(LocalDateTime.now());
                eventService.save(event);
                handler.process(event);
                eventService.updateStatus(event, UploadEvent.Status.SUCCEEDED);
            } catch (Exception e) {
                eventService.updateStatus(event, UploadEvent.Status.FAILED);
            }
        });
    }
}
