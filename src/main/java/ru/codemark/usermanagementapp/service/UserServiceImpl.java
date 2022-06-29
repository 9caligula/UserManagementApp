package ru.codemark.usermanagementapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.codemark.usermanagementapp.entity.UserEntity;
import ru.codemark.usermanagementapp.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUser(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void deleteUser(String login) {
        userRepository.deleteByLogin(login, login);
    }

    @Override
    public boolean saveUser(UserEntity user) {
        if (user.getLogin() != null && user.getName() != null && user.getPassword() != null) {
            if (user.getPassword().chars().filter(Character::isDigit).findFirst().isPresent() &&
                user.getPassword().chars().filter(Character::isUpperCase).findFirst().isPresent()) {

                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
