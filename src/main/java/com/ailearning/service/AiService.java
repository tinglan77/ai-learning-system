package com.ailearning.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.api-url}")
    private String apiUrl;

    @Value("${ai.model}")
    private String model;

    /**
     * AI 答疑
     */
    public String chat(Integer userId, String question, List<Map<String, String>> history) {
        try {
            List<Map<String, String>> messages = new ArrayList<>();
            
            // 系统提示
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是一个专业的408考研辅导助手，擅长数据结构、计算机组成原理、操作系统和计算机网络。请用简洁清晰的方式解答学生的问题，并提供学习建议。");
            messages.add(systemMsg);

            // 添加历史对话
            if (history != null) {
                messages.addAll(history);
            }

            // 添加当前问题
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", question);
            messages.add(userMsg);

            return callApi(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，AI服务暂时不可用，请稍后再试。";
        }
    }

    /**
     * AI 生成练习题
     */
    public String generateQuestion(String subject, String chapter, String type, int difficulty) {
        try {
            List<Map<String, String>> messages = new ArrayList<>();
            
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是一个408考研命题专家。请根据要求生成题目，并以JSON格式返回。");
            messages.add(systemMsg);

            String prompt = String.format(
                "请为408考研的%s科目的%s章节生成一道%s题，难度为%d（1-5）。" +
                "请以JSON格式返回，包含以下字段：\n" +
                "- content: 题目内容\n" +
                "- type: 题目类型（choice/short_answer）\n" +
                "- options: 选项（如果是选择题，格式为A.xxx B.xxx C.xxx D.xxx）\n" +
                "- answer: 正确答案\n" +
                "- analysis: 详细解析",
                subject, chapter, type.equals("choice") ? "选择题" : "简答题", difficulty
            );

            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);

            return callApi(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AI 推荐学习资源
     */
    public String recommendResources(String subject, String chapter, String weakness) {
        try {
            List<Map<String, String>> messages = new ArrayList<>();
            
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是一个408考研学习规划师。请根据学生的情况推荐学习资源和复习策略。");
            messages.add(systemMsg);

            String prompt = String.format(
                "我正在复习408考研的%s科目，当前学习%s章节。" +
                "我的薄弱环节是：%s。\n" +
                "请推荐：\n" +
                "1. 重点知识点\n" +
                "2. 学习资源（书籍、视频、练习题）\n" +
                "3. 复习策略和时间安排",
                subject, chapter, weakness != null ? weakness : "暂无特别说明"
            );

            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);

            return callApi(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，AI服务暂时不可用，请稍后再试。";
        }
    }

    /**
     * 调用 DeepSeek API
     */
    private String callApi(List<Map<String, String>> messages) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(60000);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toJSONString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                
                JSONObject responseJson = JSON.parseObject(response.toString());
                JSONArray choices = responseJson.getJSONArray("choices");
                if (choices != null && !choices.isEmpty()) {
                    JSONObject firstChoice = choices.getJSONObject(0);
                    JSONObject message = firstChoice.getJSONObject("message");
                    return message.getString("content");
                }
            }
        } else {
            throw new Exception("API调用失败，状态码：" + responseCode);
        }

        conn.disconnect();
        return null;
    }
}
