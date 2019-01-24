package com.theopus.upload.handling;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.parser.obj.FileSheet;
import com.theopus.repository.service.CircumstanceService;

import java.time.LocalDate;
import java.util.List;

public abstract class FindAndRemoveStrategy<T> {

    protected final CircumstanceService service;
    protected final FileSheet<T> parser;

    public FindAndRemoveStrategy(CircumstanceService service, FileSheet<T> parser) {
        this.service = service;
        this.parser = parser;
    }

    abstract List<Circumstance> findAndRemove(LocalDate localDate);

    abstract T getAnchor();

    abstract String getAnchorName();

}
