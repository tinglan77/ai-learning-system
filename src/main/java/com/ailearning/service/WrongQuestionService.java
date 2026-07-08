package com.ailearning.service;

import com.ailearning.entity.WrongQuestion;
import com.ailearning.mapper.WrongQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WrongQuestionService {

    @Autowired
    private WrongQuestionMapper wrongQuestionMapper;

    public List<WrongQuestion> findByUserId(Integer userId) {
        return wrongQuestionMapper.findByUserId(userId);
    }

    public WrongQuestion findById(Integer id) {
        return wrongQuestionMapper.findById(id);
    }

    public boolean addWrongQuestion(Integer userId, Integer questionId, String userAnswer, String note) {
        WrongQuestion existing = wrongQuestionMapper.findByUserIdAndQuestionId(userId, questionId);
        if (existing != null) {
            existing.setWrongCount(existing.getWrongCount() + 1);
            existing.setLastWrongTime(LocalDateTime.now());
            if (note != null) {
                existing.setNote(note);
            }
            return wrongQuestionMapper.update(existing) > 0;
        } else {
            WrongQuestion wrongQuestion = new WrongQuestion();
            wrongQuestion.setUserId(userId);
            wrongQuestion.setQuestionId(questionId);
            wrongQuestion.setUserAnswer(userAnswer);
            wrongQuestion.setWrongCount(1);
            wrongQuestion.setStatus("wrong");
            wrongQuestion.setNote(note);
            wrongQuestion.setLastWrongTime(LocalDateTime.now());
            return wrongQuestionMapper.insert(wrongQuestion) > 0;
        }
    }

    public boolean markAsCorrect(Integer id) {
        WrongQuestion wrongQuestion = wrongQuestionMapper.findById(id);
        if (wrongQuestion != null) {
            wrongQuestion.setStatus("correct");
            return wrongQuestionMapper.update(wrongQuestion) > 0;
        }
        return false;
    }

    public boolean delete(Integer id) {
        return wrongQuestionMapper.deleteById(id) > 0;
    }
}
