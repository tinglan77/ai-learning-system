package com.ailearning.controller;

import com.ailearning.common.Result;
import com.ailearning.entity.Progress;
import com.ailearning.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/user/{userId}")
    public Result<List<Progress>> getProgressByUserId(@PathVariable Integer userId) {
        return Result.success(progressService.findByUserId(userId));
    }

    @PostMapping("/update")
    public Result<String> updateProgress(@RequestBody Progress progress) {
        boolean success = progressService.updateProgress(
            progress.getUserId(),
            progress.getChapterId(),
            progress.getStatus(),
            progress.getScore(),
            progress.getStudyTime()
        );
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @PostMapping("/study-time")
    public Result<String> updateStudyTime(@RequestParam Integer userId,
                                          @RequestParam Integer chapterId,
                                          @RequestParam Integer studyTime) {
        boolean success = progressService.updateStudyTime(userId, chapterId, studyTime);
        if (success) {
            return Result.success("学习时间已更新");
        }
        return Result.error("更新失败");
    }
}
