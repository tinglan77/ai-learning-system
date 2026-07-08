package com.ailearning.service;

import com.ailearning.entity.Question;
import com.ailearning.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public Question findById(Integer id) {
        return questionMapper.findById(id);
    }

    public List<Question> findAll() {
        return questionMapper.findAll();
    }

    public List<Question> findByChapterId(Integer chapterId) {
        return questionMapper.findByChapterId(chapterId);
    }

    public List<Question> findByType(String type) {
        return questionMapper.findByType(type);
    }

    public boolean create(Question question) {
        return questionMapper.insert(question) > 0;
    }

    public boolean update(Question question) {
        return questionMapper.update(question) > 0;
    }
}
