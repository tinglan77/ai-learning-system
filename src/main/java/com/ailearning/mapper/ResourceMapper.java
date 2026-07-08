package com.ailearning.mapper;

import com.ailearning.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceMapper {
    Resource findById(@Param("id") Integer id);
    List<Resource> findAll();
    List<Resource> findByChapterId(@Param("chapterId") Integer chapterId);
    int insert(Resource resource);
    int update(Resource resource);
    int incrementViewCount(@Param("id") Integer id);
}
