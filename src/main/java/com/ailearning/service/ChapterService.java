package com.ailearning.service;

import com.ailearning.entity.Chapter;
import com.ailearning.mapper.ChapterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    public Chapter findById(Integer id) {
        return chapterMapper.findById(id);
    }

    public List<Chapter> findAll() {
        return chapterMapper.findAll();
    }

    public List<Chapter> findBySubject(String subject) {
        return chapterMapper.findBySubject(subject);
    }

    public boolean create(Chapter chapter) {
        return chapterMapper.insert(chapter) > 0;
    }

    public boolean update(Chapter chapter) {
        return chapterMapper.update(chapter) > 0;
    }
}
