package com.ailearning.controller;

import com.ailearning.common.Result;
import com.ailearning.entity.Chapter;
import com.ailearning.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/list")
    public Result<List<Chapter>> getAllChapters() {
        return Result.success(chapterService.findAll());
    }

    @GetMapping("/subject/{subject}")
    public Result<List<Chapter>> getChaptersBySubject(@PathVariable String subject) {
        return Result.success(chapterService.findBySubject(subject));
    }

    @GetMapping("/{id}")
    public Result<Chapter> getChapterById(@PathVariable Integer id) {
        Chapter chapter = chapterService.findById(id);
        if (chapter != null) {
            return Result.success(chapter);
        }
        return Result.error("章节不存在");
    }

    @PostMapping("/create")
    public Result<String> createChapter(@RequestBody Chapter chapter) {
        boolean success = chapterService.create(chapter);
        if (success) {
            return Result.success("创建成功");
        }
        return Result.error("创建失败");
    }

    @PutMapping("/update")
    public Result<String> updateChapter(@RequestBody Chapter chapter) {
        boolean success = chapterService.update(chapter);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
}
