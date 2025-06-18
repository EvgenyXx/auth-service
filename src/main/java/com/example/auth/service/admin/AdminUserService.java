package com.example.auth.service.admin;

import com.example.auth.dto.BlockUserRequest;
import com.example.auth.dto.UserDto;
import com.example.auth.exception.user.UserNotFoundException;
import org.springframework.data.domain.Page;

import javax.management.relation.RoleNotFoundException;
import javax.security.auth.login.AccountLockedException;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис для административного управления пользователями
 */
public interface AdminUserService {

    /**
     * Блокирует или разблокирует пользовательский аккаунт
     * @param request Дто для блокировки пользователя
     * @throws UserNotFoundException если пользователь не найден
     * @throws IllegalStateException если попытка заблокировать администратора
     */
    void setUserBlockStatus(BlockUserRequest request);

    /**
     * Обновляет роли пользователя
     * @param userId UUID пользователя (обязательно)
     * @param roles Набор новых ролей (минимум 1 роль)
     * @throws UserNotFoundException если пользователь не найден
     * @throws RoleNotFoundException если указаны несуществующие роли
     * @throws IllegalArgumentException если roles пустой
     */
    void updateUserRoles(UUID userId, Set<String> roles);

    /**
     * Возвращает страницу с пользователями, отфильтрованными по поисковому запросу
     * @param searchQuery Поисковая строка (email/имя, необязательно)
     * @param page Номер страницы (начиная с 0)
     * @param size Количество элементов на странице (1-100)
     * @return Страница с UserDto (никогда не null)
     * @throws IllegalArgumentException если page < 0 или size вне допустимого диапазона
     */
    Page<UserDto> getUsers(String searchQuery, int page, int size);

    /**
     * Генерирует и устанавливает временный пароль для пользователя
     * @param userId UUID пользователя (обязательно)
     * @return Временный пароль в открытом виде (8-12 символов)
     * @throws UserNotFoundException если пользователь не найден
     * @throws AccountLockedException если аккаунт заблокирован
     */
    String resetUserPassword(UUID userId);
}