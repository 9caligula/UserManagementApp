package ru.codemark.usermanagementapp.service;

import ru.codemark.usermanagementapp.entity.UserEntity;

import java.util.List;


public interface UserService {

    List<UserEntity> getAllUsers();

    UserEntity getUser(String login);

    void deleteUser(String login);

    boolean saveUser(UserEntity user);

}
