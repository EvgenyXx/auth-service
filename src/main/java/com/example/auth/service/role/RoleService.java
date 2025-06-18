package com.example.auth.service.role;

import com.example.auth.entity.Role;

import java.util.Collection;
import java.util.Set;

public interface RoleService {

    Role getDefaultUserRole();

    Set<Role>findByName(Collection<String>name);
}
