package com.ailearning.service;

import com.ailearning.entity.User;
import com.ailearning.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean register(User user) {
        User existing = userMapper.findByUsername(user.getUsername());
        if (existing != null) {
            return false;
        }
        user.setRole("user");
        userMapper.insert(user);
        return true;
    }

    public boolean update(User user) {
        return userMapper.update(user) > 0;
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }
}
