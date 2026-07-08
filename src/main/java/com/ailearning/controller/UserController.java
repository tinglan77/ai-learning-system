package com.ailearning.controller;

import com.ailearning.common.Result;
import com.ailearning.entity.User;
import com.ailearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        User user = userService.login(username, password);
        if (user != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("token", "mock-token-" + user.getId());
            return Result.success(result);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            return Result.success("注册成功");
        }
        return Result.error("用户名已存在");
    }

    @GetMapping("/info/{id}")
    public Result<User> getUserInfo(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        boolean success = userService.update(user);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @GetMapping("/list")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }
}
