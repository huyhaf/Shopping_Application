package com.huyhaf.shopapp.sevices;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huyhaf.shopapp.models.Role;
import com.huyhaf.shopapp.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
