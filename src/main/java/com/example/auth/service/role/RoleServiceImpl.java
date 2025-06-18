package com.example.auth.service.role;

import com.example.auth.entity.DefaultRole;
import com.example.auth.entity.Role;
import com.example.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getDefaultUserRole() {
        return roleRepository.findByName(DefaultRole.USER.getName());
    }

    @Override
    public Set<Role> findByName(Collection<String> name) {
        return roleRepository.findByNameIn(name);
    }
}
