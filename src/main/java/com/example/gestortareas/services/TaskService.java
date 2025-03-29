package com.example.gestortareas.services;

import com.example.gestortareas.data.models.Task;
import com.example.gestortareas.respositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService extends ApiService<Task> {

    public TaskService(TaskRepository taskRepository) {
        super(taskRepository);
    }

}
