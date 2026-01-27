package org.qinyu.service;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.qinyu.handler.exception.BaseException;
import org.qinyu.model.UserModel;
import org.qinyu.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsManager {
    private final UserRepository repository;

    public UserModel findById(Integer id) {
        Optional<UserModel> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new BaseException("id 为 " + id + " 的用户不存在");
        }
        return optional.get();
    }

    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        UserModel model = UserModel.withEmail(username);
        Optional<UserModel> optional = repository.findOne(Example.of(model));
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("邮箱或密码有误");
        }
        return optional.get();
    }

    @Override
    public void createUser(@NonNull UserDetails user) {
        UserModel model = (UserModel) user;
        try {
            loadUserByUsername(model.getEmail());
        } catch (UsernameNotFoundException exception) {
            repository.save(model);
            return;
        }
        throw new AuthenticationServiceException("用户 " + model.getEmail() + " 已存在");
    }

    @Override
    public void updateUser(@NonNull UserDetails user) {

    }

    @Override
    public void deleteUser(@NonNull String username) {

    }

    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword) {

    }

    @Override
    public boolean userExists(@NonNull String username) {
        UserModel model = UserModel.withEmail(username);
        Example<UserModel> example = Example.of(model);
        return repository.findOne(example).isPresent();
    }
}
