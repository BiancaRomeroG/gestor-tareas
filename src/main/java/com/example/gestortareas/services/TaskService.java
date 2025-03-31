package com.example.gestortareas.services;

import com.example.gestortareas.data.models.Task;
import com.example.gestortareas.respositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService extends ApiService<Task> {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        super(taskRepository);
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByAssignedUserId(userId);
    }

}
