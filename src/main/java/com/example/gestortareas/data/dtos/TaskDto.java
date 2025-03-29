package com.example.gestortareas.data.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private String status;
    private Long assignedUserId;
}
