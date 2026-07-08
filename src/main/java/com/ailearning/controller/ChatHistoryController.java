package com.ailearning.controller;

import com.ailearning.common.Result;
import com.ailearning.entity.ChatHistory;
import com.ailearning.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @GetMapping("/history/{userId}")
    public Result<List<ChatHistory>> getChatHistory(@PathVariable Integer userId) {
        return Result.success(chatHistoryService.findByUserId(userId));
    }

    @PostMapping("/save")
    public Result<String> saveMessage(@RequestParam Integer userId,
                                      @RequestParam String role,
                                      @RequestParam String content) {
        boolean success = chatHistoryService.saveMessage(userId, role, content);
        if (success) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }

    @DeleteMapping("/clear/{userId}")
    public Result<String> clearHistory(@PathVariable Integer userId) {
        boolean success = chatHistoryService.clearHistory(userId);
        if (success) {
            return Result.success("历史记录已清空");
        }
        return Result.error("清空失败");
    }
}
