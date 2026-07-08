package com.ailearning.mapper;

import com.ailearning.entity.Progress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProgressMapper {
    Progress findById(@Param("id") Integer id);
    List<Progress> findByUserId(@Param("userId") Integer userId);
    Progress findByUserIdAndChapterId(@Param("userId") Integer userId, @Param("chapterId") Integer chapterId);
    int insert(Progress progress);
    int update(Progress progress);
}
