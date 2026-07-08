package com.ailearning.mapper;

import com.ailearning.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findById(@Param("id") Integer id);
    User findByUsername(@Param("username") String username);
    int insert(User user);
    int update(User user);
    List<User> findAll();
}
