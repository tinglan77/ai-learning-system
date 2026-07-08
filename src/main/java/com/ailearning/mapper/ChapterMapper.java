package com.ailearning.mapper;

import com.ailearning.entity.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChapterMapper {
    Chapter findById(@Param("id") Integer id);
    List<Chapter> findAll();
    List<Chapter> findBySubject(@Param("subject") String subject);
    int insert(Chapter chapter);
    int update(Chapter chapter);
}
