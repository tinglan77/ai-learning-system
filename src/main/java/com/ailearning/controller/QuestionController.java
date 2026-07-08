package com.ailearning.controller;

import com.ailearning.common.Result;
import com.ailearning.entity.Question;
import com.ailearning.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/list")
    public Result<List<Question>> getAllQuestions() {
        return Result.success(questionService.findAll());
    }

    @GetMapping("/chapter/{chapterId}")
    public Result<List<Question>> getQuestionsByChapterId(@PathVariable Integer chapterId) {
        return Result.success(questionService.findByChapterId(chapterId));
    }

    @GetMapping("/type/{type}")
    public Result<List<Question>> getQuestionsByType(@PathVariable String type) {
        return Result.success(questionService.findByType(type));
    }

    @GetMapping("/{id}")
    public Result<Question> getQuestionById(@PathVariable Integer id) {
        Question question = questionService.findById(id);
        if (question != null) {
            return Result.success(question);
        }
        return Result.error("题目不存在");
    }

    @PostMapping("/create")
    public Result<String> createQuestion(@RequestBody Question question) {
        boolean success = questionService.create(question);
        if (success) {
            return Result.success("创建成功");
        }
        return Result.error("创建失败");
    }

    @PutMapping("/update")
    public Result<String> updateQuestion(@RequestBody Question question) {
        boolean success = questionService.update(question);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
}
