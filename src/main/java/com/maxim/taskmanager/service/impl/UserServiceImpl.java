package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        // Проверяем, не занят ли email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с email " + user.getEmail() + " уже существует");
        }

        // TODO: здесь будет шифрование пароля (когда добавим Security)

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(Integer id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + id + " не найден"));

        // Обновляем поля
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setAge(userDetails.getAge());

        // Email обновляем только если он передан и отличается от текущего
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(existingUser.getEmail())) {
            // Проверяем, не занят ли новый email
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email " + userDetails.getEmail() + " уже используется");
            }
            existingUser.setEmail(userDetails.getEmail());
        }

        // Пароль обновляем только если он передан (не null и не пустой)
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            // TODO: здесь будет шифрование пароля
            existingUser.setPassword(userDetails.getPassword());
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
