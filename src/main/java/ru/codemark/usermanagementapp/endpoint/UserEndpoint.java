package ru.codemark.usermanagementapp.endpoint;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.codemark.usermanagementapp.entity.RoleEntity;
import ru.codemark.usermanagementapp.entity.UserEntity;
import ru.codemark.usermanagementapp.service.UserServiceImpl;
import ru.codemark.usermanagementapp.soap.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Endpoint
public class UserEndpoint {

    private static final String NAMESPACE_URI = "http://soap.usermanagementapp.codemark.ru";

    @Autowired
    private UserServiceImpl userService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllUsersRequest")
    @ResponsePayload
    public GetAllUsersResponse getAllUsers() {
        GetAllUsersResponse response = new GetAllUsersResponse();
        List<User> list = new ArrayList<>();
        List<UserEntity> userEntityList = userService.getAllUsers();
        for (UserEntity userEntity : userEntityList) {
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            list.add(user);
        }
        response.getUsers().addAll(list);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByLoginRequest")
    @ResponsePayload
    public GetUserByLoginResponse getUserByLogin(@RequestPayload GetUserByLoginRequest request) {
        GetUserByLoginResponse response = new GetUserByLoginResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        UserEntity userEntity = userService.getUser(request.getLogin());
        if (userEntity == null) {
            serviceStatus.setStatus("false");
            serviceStatus.setMessage("User not found");
            response.setServiceStatus(serviceStatus);
        } else {
            User user = new User();
            Set<Role> set = new HashSet<>();
            BeanUtils.copyProperties(userEntity, user);
            for (RoleEntity roleEntity: userEntity.getRoles()) {
                Role role = new Role();
                BeanUtils.copyProperties(roleEntity, role);
                set.add(role);
            }
            user.getRoles().addAll(set);
            response.setUser(user);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {
        DeleteUserResponse response = new DeleteUserResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        if (userService.getUser(request.getLogin()) == null) {
            serviceStatus.setStatus("false");
            serviceStatus.setMessage("User not found");
            response.setServiceStatus(serviceStatus);
        } else {
            userService.deleteUser(request.getLogin());
            serviceStatus.setStatus("true");
            serviceStatus.setMessage("Content Deleted Successfully");
        }
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserRequest")
    @ResponsePayload
    public AddUserResponse addEmployee(@RequestPayload AddUserRequest request) {
        AddUserResponse response = new AddUserResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        UserEntity userEntity = new UserEntity();
        // если пользователь уже существует с такми логином, то произойдет перезаписывание, в данном методе это исключено
        // поэтому на response отправляем сразу false
        if (userService.getUser(request.getUser().getLogin()) == null) {
            BeanUtils.copyProperties(request.getUser(), userEntity);
            for (Role roles: request.getUser().getRoles()) {
                RoleEntity roleEntity = new RoleEntity();
                BeanUtils.copyProperties(roles, roleEntity);
                userEntity.getRoles().add(roleEntity);
            }

            boolean flag = userService.saveUser(userEntity);
            if (flag) {
                User user = new User();
                BeanUtils.copyProperties(userEntity, user);
                response.setUser(user);
                serviceStatus.setStatus("true");
                serviceStatus.setMessage("Content Added Successfully");

            } else {
                serviceStatus.setStatus("false");
                serviceStatus.setMessage("Validation for adding failed");
            }
        } else {
            serviceStatus.setStatus("false");
            serviceStatus.setMessage("User already exists with this login");
        }

        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {
        UpdateUserResponse response = new UpdateUserResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        if (userService.getUser(request.getUser().getLogin()) != null) {
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(request.getUser(), userEntity);
            for (Role roles: request.getUser().getRoles()) {
                RoleEntity roleEntity = new RoleEntity();
                BeanUtils.copyProperties(roles, roleEntity);
                userEntity.getRoles().add(roleEntity);
            }

            boolean flag = userService.saveUser(userEntity);
            if (flag) {
                serviceStatus.setStatus("true");
                serviceStatus.setMessage("Content Added Successfully");
            } else {
                serviceStatus.setStatus("false");
                serviceStatus.setMessage("Validation for adding failed");
            }
        } else {
            serviceStatus.setStatus("false");
            serviceStatus.setMessage("User not found");
        }

        response.setServiceStatus(serviceStatus);
        return response;
    }
}
