package com.example.connectify.repository;

import com.example.connectify.domain.Entity;

import java.util.Map;



public interface Repository<ID, E extends Entity<ID>> {

    E findOne(ID id);

    Map<ID,E> getEntities();

    E save(E entity);

    E delete(ID id);

    E update(E entity);
}