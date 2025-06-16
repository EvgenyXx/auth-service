package com.example.auth.service.admin;

import com.example.auth.dto.BlockUserRequest;
import com.example.auth.dto.UserDto;
import com.example.auth.entity.DefaultRole;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.exception.user.AdminBlockingForbiddenException;
import com.example.auth.service.role.RoleService;
import com.example.auth.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Log4j2
public class AdminUserServiceImpl implements AdminUserService {
    private final UserService userService;
    private final RoleService roleService;

    public AdminUserServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @Transactional
    @Override
    public void setUserBlockStatus(BlockUserRequest request) {
        User user = userService.findById(request.userId());
        if (hasAdminRole(user)){
            log.warn("Попытка заблокировать пользователя с идентификатором: {}", request.userId());
            //TODO ДОБАВЬ В ЦЕНТРАЛЬНЫЙ ОБРАБОТЧИК
            throw new AdminBlockingForbiddenException("Нельзя заблокировать администратора");
        }
        user.setBlocked(request.isBlocked());
        userService.saveUser(user);
    }

    private boolean hasAdminRole(User user){
        return user.getRoles().stream().map(Role::getName)
                .anyMatch(role ->role.equals(DefaultRole.ADMIN.getName()));
    }

    @Override
    public void updateUserRoles(UUID userId, Set<String> roles) {
        //найти пользователя
        //добавить роли
        //сохранить
        //TODO ДОДЕЛАТЬ МЕТОД
        User user = userService.findById(userId);
        Set<Role>roleSet = roleService.findByName(roles);
        user.setRoles(roleSet);
        userService.saveUser(user);

    }

    @Override
    public Page<UserDto> getUsers(String searchQuery, int page, int size) {
        return null;
    }

    @Override
    public String resetUserPassword(UUID userId) {
        return "";
    }
}
