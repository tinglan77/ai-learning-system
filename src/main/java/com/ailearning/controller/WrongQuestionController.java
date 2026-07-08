package com.ailearning.controller;

import com.ailearning.common.Result;
import com.ailearning.entity.WrongQuestion;
import com.ailearning.service.WrongQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wrong-question")
public class WrongQuestionController {

    @Autowired
    private WrongQuestionService wrongQuestionService;

    @GetMapping("/user/{userId}")
    public Result<List<WrongQuestion>> getWrongQuestionsByUserId(@PathVariable Integer userId) {
        return Result.success(wrongQuestionService.findByUserId(userId));
    }

    @PostMapping("/add")
    public Result<String> addWrongQuestion(@RequestParam Integer userId,
                                           @RequestParam Integer questionId,
                                           @RequestParam String userAnswer,
                                           @RequestParam(required = false) String note) {
        boolean success = wrongQuestionService.addWrongQuestion(userId, questionId, userAnswer, note);
        if (success) {
            return Result.success("已加入错题本");
        }
        return Result.error("添加失败");
    }

    @PutMapping("/correct/{id}")
    public Result<String> markAsCorrect(@PathVariable Integer id) {
        boolean success = wrongQuestionService.markAsCorrect(id);
        if (success) {
            return Result.success("已标记为正确");
        }
        return Result.error("标记失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> deleteWrongQuestion(@PathVariable Integer id) {
        boolean success = wrongQuestionService.delete(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
