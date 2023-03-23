package com.example.connectify.repository.memory;


import com.example.connectify.domain.Entity;
import com.example.connectify.domain.validators.Validator;
import com.example.connectify.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private Validator<E> validator;
    Map<ID, E> entities;


    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }


    public Validator<E> getValidator() {
        return validator;
    }


    public void setValidator(Validator<E> validator) {
        this.validator = validator;
    }


    public Map<ID, E> getEntities() {
        return entities;
    }


    public void setEntities(Map<ID, E> entities) {
        this.entities = entities;
    }



    @Override
    public E findOne(ID id) {
        if (id==null)
            throw new IllegalArgumentException("ID must be not null");
        if(entities.get(id)==null)
            return null;
        return entities.get(id);
    }



    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        entities.put(entity.getId(),entity);
        return null;
    }



    @Override
    public E delete(ID id) {
        if(entities.get(id)==null)
            return null;
        else entities.remove(id);
        return null;
    }



    @Override
    public E update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null!");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null)
        {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }
}