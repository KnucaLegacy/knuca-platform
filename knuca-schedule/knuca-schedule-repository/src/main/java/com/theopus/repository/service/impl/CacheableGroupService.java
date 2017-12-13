package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Group;
import com.theopus.repository.jparepo.GroupRepository;
import com.theopus.repository.service.SimpleService;
import com.theopus.repository.specification.GroupSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CacheableGroupService implements SimpleService<Group> {

    private GroupRepository repository;

    @Autowired
    public CacheableGroupService(GroupRepository repository) {
        this.repository = repository;
    }

    @Cacheable
    @Override
    public Group save(Group group){
        Group saved = findByName(group.getName());
        if (saved != null){
            return saved;
        }
        return repository.save(group);
    }

    @Cacheable
    @Override
    public Group save(String name){
        Group group = findByName(name);
        if (group != null) {
            return group;
        }
        group = new Group();
        group.setName(name);
        return repository.save(group);
    }

    @Cacheable
    @Override
    public Group findByName(String name){
        return (Group) repository.findOne(GroupSpecification.getByName(name));
    }

    @Override
    public Long size() {
        return repository.count();
    }

    @Override
    public Collection<Group> getAll(){
        return repository.findAll();
    }
}
