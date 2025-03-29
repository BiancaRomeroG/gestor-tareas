package com.example.gestortareas.controllers;

import com.example.gestortareas.data.dtos.TaskDto;
import com.example.gestortareas.data.models.Task;
import com.example.gestortareas.data.models.User;
import com.example.gestortareas.data.responses.ApiResponse;
import com.example.gestortareas.services.TaskService;
import com.example.gestortareas.services.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController extends ApiController<Task, TaskDto> {

    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        super(taskService);
        this.userService = userService;
    }

    @Override
    protected Task transformDtoToEntity(TaskDto dto) {
        User assignedUser = null;
        if (dto.getAssignedUserId() != null) {
            assignedUser = userService.getById(dto.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getAssignedUserId()));
        }
        return Task.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .assignedUser(assignedUser)
                .build();
    }

    @Override
    protected TaskDto transformEntityToDto(Task entity) {
        Long assignedUserId = (entity.getAssignedUser() != null) ? entity.getAssignedUser().getId() : null;
        return TaskDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .assignedUserId(assignedUserId)
                .build();
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<TaskDto>> saveOrUpdate(@RequestBody TaskDto requestDto) {
        ApiResponse<TaskDto> response = new ApiResponse<>();
        HttpStatus status = HttpStatus.OK;

        try {
            Task entity = transformDtoToEntity(requestDto);
            Task savedEntity = service.save(entity);
            TaskDto savedDto = transformEntityToDto(savedEntity);

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
