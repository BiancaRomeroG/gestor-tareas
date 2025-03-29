package com.example.gestortareas.data.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRoleDto {

    private Long id;
    private Long userId;
    private Long roleId;
}
