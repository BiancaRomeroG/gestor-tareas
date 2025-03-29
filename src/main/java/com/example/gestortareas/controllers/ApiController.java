package com.example.gestortareas.controllers;

import com.example.gestortareas.data.responses.ApiResponse;
import com.example.gestortareas.services.ApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ApiController<T, K> {

    protected final ApiService<T> service;

    public ApiController(ApiService<T> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<K>>> getAll() {
        ApiResponse<List<K>> response = new ApiResponse<>();
        HttpStatus status = HttpStatus.OK;

        try {
            List<T> entities = service.getAll();
            List<K> dtos = transformEntitiesToDtos(entities);

            response.setPayload(dtos);
            response.setMessage("Operación exitosa");
            response.setStatusCode(HttpStatus.OK.value());
        } catch (Exception e) {
            response.setMessage(e.getMessage() != null ? e.getMessage() : "Error desconocido");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<K>> getById(@PathVariable("id") Long id) {
        ApiResponse<K> response = new ApiResponse<>();
        HttpStatus status = HttpStatus.OK;

        try {
            Optional<T> entityOpt = service.getById(id);
            if (entityOpt.isEmpty()) {
                throw new Exception("Elemento no encontrado con ID: " + id);
            }

            K dto = transformEntityToDto(entityOpt.get());
            response.setPayload(dto);
            response.setMessage("Operación exitosa");
            response.setStatusCode(HttpStatus.OK.value());
        } catch (Exception e) {
            response.setMessage(e.getMessage() != null ? e.getMessage() : "Error desconocido");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<K>> saveOrUpdate(@RequestBody K requestDto) {
        ApiResponse<K> response = new ApiResponse<>();
        HttpStatus status = HttpStatus.OK;

        try {
            T entity = transformDtoToEntity(requestDto);
            T savedEntity = service.save(entity);
            K savedDto = transformEntityToDto(savedEntity);

            response.setPayload(savedDto);
            response.setMessage("Operación exitosa");
            response.setStatusCode(HttpStatus.OK.value());
        } catch (Exception e) {
            response.setMessage(e.getMessage() != null ? e.getMessage() : "Error");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<K>> deleteById(@PathVariable("id") Long id) {
        ApiResponse<K> response = new ApiResponse<>();
        HttpStatus status = HttpStatus.OK;

        try {
            Optional<T> entityOpt = service.getById(id);
            if (entityOpt.isEmpty()) {
                throw new Exception("Elemento no encontrado con ID: " + id);
            }

            service.delete(entityOpt.get());
            response.setMessage("Operación exitosa");
            response.setStatusCode(HttpStatus.OK.value());
        } catch (Exception e) {
            response.setMessage(e.getMessage() != null ? e.getMessage() : "Error");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }

    protected List<K> transformEntitiesToDtos(List<T> entities) {
        List<K> dtos = new ArrayList<>();
        for (T entity : entities) {
            dtos.add(transformEntityToDto(entity));
        }
        return dtos;
    }

    protected T transformDtoToEntity(K dto) {
        throw new UnsupportedOperationException("Transformación DTO -> Entidad no implementada");
    }

    protected K transformEntityToDto(T entity) {
        throw new UnsupportedOperationException("Transformación Entidad -> DTO no implementada");
    }
}
