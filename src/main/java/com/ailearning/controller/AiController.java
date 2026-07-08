package com.ailearning.controller;

import com.ailearning.common.Result;
import com.ailearning.service.AiService;
import com.ailearning.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @Autowired
    private ChatHistoryService chatHistoryService;

    @PostMapping("/chat")
    public Result<Map<String, String>> chat(@RequestBody Map<String, Object> request) {
        Integer userId = (Integer) request.get("userId");
        String question = (String) request.get("question");

        // 保存用户消息
        chatHistoryService.saveMessage(userId, "user", question);

        // 获取历史记录
        List<Map<String, String>> history = chatHistoryService.findByUserId(userId)
                .stream()
                .map(h -> {
                    Map<String, String> msg = new HashMap<>();
                    msg.put("role", h.getRole());
                    msg.put("content", h.getContent());
                    return msg;
                })
                .toList();

        // 调用AI
        String answer = aiService.chat(userId, question, history);

        // 保存AI回复
        chatHistoryService.saveMessage(userId, "assistant", answer);

        Map<String, String> result = new HashMap<>();
        result.put("answer", answer);
        return Result.success(result);
    }

    @PostMapping("/generate-question")
    public Result<Map<String, String>> generateQuestion(@RequestBody Map<String, Object> request) {
        String subject = (String) request.get("subject");
        String chapter = (String) request.get("chapter");
        String type = (String) request.get("type");
        int difficulty = (int) request.get("difficulty");

        String result = aiService.generateQuestion(subject, chapter, type, difficulty);
        if (result != null) {
            Map<String, String> data = new HashMap<>();
            data.put("content", result);
            return Result.success(data);
        }
        return Result.error("生成题目失败");
    }

    @PostMapping("/recommend")
    public Result<Map<String, String>> recommendResources(@RequestBody Map<String, Object> request) {
        String subject = (String) request.get("subject");
        String chapter = (String) request.get("chapter");
        String weakness = (String) request.get("weakness");

        String result = aiService.recommendResources(subject, chapter, weakness);
        Map<String, String> data = new HashMap<>();
        data.put("recommendation", result);
        return Result.success(data);
    }
}
