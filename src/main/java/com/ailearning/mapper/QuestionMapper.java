package com.ailearning.mapper;

import com.ailearning.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    Question findById(@Param("id") Integer id);
    List<Question> findAll();
    List<Question> findByChapterId(@Param("chapterId") Integer chapterId);
    List<Question> findByType(@Param("type") String type);
    int insert(Question question);
    int update(Question question);
}
