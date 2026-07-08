package com.ailearning.mapper;

import com.ailearning.entity.WrongQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WrongQuestionMapper {
    WrongQuestion findById(@Param("id") Integer id);
    List<WrongQuestion> findByUserId(@Param("userId") Integer userId);
    WrongQuestion findByUserIdAndQuestionId(@Param("userId") Integer userId, @Param("questionId") Integer questionId);
    int insert(WrongQuestion wrongQuestion);
    int update(WrongQuestion wrongQuestion);
    int incrementWrongCount(@Param("id") Integer id);
    int deleteById(@Param("id") Integer id);
}
