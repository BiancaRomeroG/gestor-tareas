package com.example.gestortareas.controllers;

import com.example.gestortareas.data.dtos.UserDto;
import com.example.gestortareas.data.models.User;
import com.example.gestortareas.data.responses.ApiResponse;
import com.example.gestortareas.services.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/users")
public class UserController extends ApiController<User, UserDto> {

    public UserController(UserService userService) {
        super(userService);
    }

    @Override
    protected User transformDtoToEntity(UserDto dto) {
        String hashedPassword = null;
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            try {
                hashedPassword = sha256(dto.getPassword());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error al encriptar contraseña: " + e.getMessage(), e);
            }
        } else {
            hashedPassword = "";
        }

        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(hashedPassword)
                .build();
    }

    @Override
    protected UserDto transformEntityToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> saveOrUpdate(@RequestBody UserDto requestDto) {
        ApiResponse<UserDto> response = new ApiResponse<>();
        HttpStatus status = HttpStatus.OK;

        try {
            User entity = transformDtoToEntity(requestDto);
            User savedEntity = service.save(entity);
            UserDto savedDto = transformEntityToDto(savedEntity);

            response.setPayload(savedDto);
            response.setMessage("Operación exitosa");
            response.setStatusCode(HttpStatus.OK.value());

        } catch (DataIntegrityViolationException dive) {
            response.setMessage("El correo electrónico ya está registrado.");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            response.setMessage(e.getMessage() != null ? e.getMessage() : "Error");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }


    private String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
