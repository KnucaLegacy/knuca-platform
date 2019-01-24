package com.theopus.upload.handling;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.obj.FileSheet;
import com.theopus.repository.service.CircumstanceService;
import com.theopus.repository.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FindAndRemoveGroupStrategy extends FindAndRemoveStrategy<Group> {

    private static final Logger LOG = LoggerFactory.getLogger(FindAndRemoveGroupStrategy.class);
    private final GroupService groupService;

    public FindAndRemoveGroupStrategy(GroupService groupService, CircumstanceService service, FileSheet<Group> parser) {
        super(service, parser);
        this.groupService = groupService;
    }

    @Override
    public List<Circumstance> findAndRemove(LocalDate localDate) {
        Group raw = getAnchor();
        Group anchor = groupService.findByName(raw.getName());
        if (Objects.isNull(anchor)) {
            LOG.info("Group anchor with name {} not found in db.", raw.getName());
            return Collections.emptyList();
        }
        List<Circumstance> deleted = service.deleteWithGroupAfter(anchor, localDate);
        LOG.info("Cleared with anchor {}, {} circumstances", anchor, deleted.size());
        return deleted;
    }

    @Override
    Group getAnchor() {
        return parser.getCurrent().getAnchor();
    }

    @Override
    String getAnchorName() {
        return parser.getCurrent().getAnchor().getName();
    }
}
