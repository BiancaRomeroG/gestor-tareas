package com.example.gestortareas.controllers;

import com.example.gestortareas.data.dtos.LoginRequest;
import com.example.gestortareas.data.dtos.UserDto;
import com.example.gestortareas.data.responses.ApiResponse;
import com.example.gestortareas.data.models.User;
import com.example.gestortareas.services.JwtService;
import com.example.gestortareas.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;
    private final JwtService jwtService;

    public LoginController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        try {
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());

            if (userService.checkPassword(loginRequest.getPassword(), userDetails.getPassword())) {
                String token = jwtService.generateToken(userDetails.getUsername());

                User user = userService.getByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + loginRequest.getEmail()));

                UserDto userDto = UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build();

                Map<String, Object> payload = new HashMap<>();
                payload.put("JWT", token);
                payload.put("user", userDto);

                response.setPayload(payload);
                response.setMessage("Login exitoso");
                response.setStatusCode(HttpStatus.OK.value());

                return ResponseEntity.ok(response);
            } else {
                response.setMessage("Credenciales incorrectas");
                response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (UsernameNotFoundException e) {
            response.setMessage("Usuario no encontrado");
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Error interno del servidor");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
