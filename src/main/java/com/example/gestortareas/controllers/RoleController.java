package com.example.gestortareas.controllers;

import com.example.gestortareas.data.dtos.RoleDto;
import com.example.gestortareas.data.models.Role;
import com.example.gestortareas.data.responses.ApiResponse;
import com.example.gestortareas.services.RoleService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends ApiController<Role, RoleDto> {

    public RoleController(RoleService roleService) {
        super(roleService);
    }

    @Override
    protected Role transformDtoToEntity(RoleDto dto) {
        return Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    @Override
    protected RoleDto transformEntityToDto(Role entity) {
        return RoleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<RoleDto>> saveOrUpdate(@RequestBody RoleDto requestDto) {
        ApiResponse<RoleDto> response = new ApiResponse<>();
        HttpStatus status = HttpStatus.OK;

        try {
            Role entity = transformDtoToEntity(requestDto);
            Role savedEntity = service.save(entity);
            RoleDto savedDto = transformEntityToDto(savedEntity);

            response.setPayload(savedDto);
            response.setMessage("Operación exitosa");
            response.setStatusCode(HttpStatus.OK.value());

        } catch (Exception e) {
            response.setMessage(e.getMessage() != null ? e.getMessage() : "Error");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }
}
