package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Group;
import com.theopus.repository.jparepo.GroupRepository;
import com.theopus.repository.service.GroupService;
import com.theopus.repository.specification.GroupSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

public class CacheableGroupService implements GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableGroupService.class);

    private GroupRepository repository;

    public CacheableGroupService(GroupRepository repository) {
        this.repository = repository;
    }

    @Cacheable("groups")
    @Override
    public Group save(Group group) {
        Group saved = findByName(group.getName());
        if (saved != null) {
            return saved;
        }
        return repository.save(group);
    }

    @Cacheable("groups")
    @Override
    public Group findByName(String name) {
        return (Group) repository.findOne(GroupSpecification.getByName(name));
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Collection<Group> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Group object) {

    }

    @Override
    @CacheEvict(value = "groups", allEntries = true)
    public void flush() {
        LOG.info("Cleared 'groups' cache.");
    }
}
