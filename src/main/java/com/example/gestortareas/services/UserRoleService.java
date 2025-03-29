package com.example.gestortareas.services;

import com.example.gestortareas.data.models.UserRole;
import com.example.gestortareas.respositories.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends ApiService<UserRole> {

    public UserRoleService(UserRoleRepository userRoleRepository) {
        super(userRoleRepository);
    }

}
