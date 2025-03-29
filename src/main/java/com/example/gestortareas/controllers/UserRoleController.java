package com.example.gestortareas.controllers;

import com.example.gestortareas.data.dtos.UserRoleDto;
import com.example.gestortareas.data.models.Role;
import com.example.gestortareas.data.models.User;
import com.example.gestortareas.data.models.UserRole;
import com.example.gestortareas.data.responses.ApiResponse;
import com.example.gestortareas.services.RoleService;
import com.example.gestortareas.services.UserRoleService;
import com.example.gestortareas.services.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController extends ApiController<UserRole, UserRoleDto> {

    private final UserService userService;
    private final RoleService roleService;

    public UserRoleController(UserRoleService userRoleService,
                              UserService userService,
                              RoleService roleService) {
        super(userRoleService);
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    protected UserRole transformDtoToEntity(UserRoleDto dto) {
        User user = userService.getById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUserId()));

        Role role = roleService.getById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.getRoleId()));

        return UserRole.builder()
                .id(dto.getId())
                .user(user)
                .role(role)
                .build();
    }

    @Override
    protected UserRoleDto transformEntityToDto(UserRole entity) {
        return UserRoleDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .roleId(entity.getRole().getId())
                .build();
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<UserRoleDto>> saveOrUpdate(@RequestBody UserRoleDto requestDto) {
        ApiResponse<UserRoleDto> response = new ApiResponse<>();
        HttpStatus status = HttpStatus.OK;

        try {
            UserRole entity = transformDtoToEntity(requestDto);
            UserRole savedEntity = service.save(entity);
            UserRoleDto savedDto = transformEntityToDto(savedEntity);

            response.setPayload(savedDto);
            response.setMessage("Operación exitosa");
            response.setStatusCode(HttpStatus.OK.value());
        } catch (DataIntegrityViolationException dive) {
            String dbMessage = dive.getMessage();
            response.setMessage("Violación de integridad: " + dbMessage);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            status = HttpStatus.BAD_REQUEST;
        } catch (RuntimeException re) {
            response.setMessage(re.getMessage());
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            status = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            response.setMessage(e.getMessage() != null ? e.getMessage() : "Error");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }
}
