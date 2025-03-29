package com.example.gestortareas.services;

import com.example.gestortareas.data.models.Role;
import com.example.gestortareas.respositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ApiService<Role> {

    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
    }

}
