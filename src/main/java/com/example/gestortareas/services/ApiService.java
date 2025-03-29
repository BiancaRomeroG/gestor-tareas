package com.example.gestortareas.services;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public abstract class ApiService<T> {

    protected final JpaRepository<T, Long> repository;

    public ApiService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }
}
