# AI Learning System

面向 **408 考研** 的智能学习伙伴系统，根据学习进度推荐学习资源、AI 生成练习题、答疑解惑，并提供错题本与学习统计等功能。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Java 17、Spring Boot 3.2、MyBatis、MySQL |
| 前端 | Vue 3、Vite、Element Plus、Pinia、ECharts |
| AI | DeepSeek Chat API |

## 核心功能

### 用户与认证
- 用户注册 / 登录
- 个人资料管理（昵称、头像上传等）

### 学习管理
- **首页**：四门科目（数据结构、计组、操作系统、计算机网络）学习进度概览、今日任务、快速入口
- **资源中心**：按章节浏览学习资源（文章、视频），支持按科目筛选
- **学习进度**：记录各章节学习状态（未开始 / 进行中 / 已完成）、得分与学习时长

### AI 能力
- **AI 问答**：408 考研专业答疑，支持多轮对话，历史记录持久化
- **AI 练习**：按科目、章节、题型（选择题 / 简答题）、难度自动生成练习题
- **AI 推荐**：根据当前章节与薄弱环节，推荐知识点、学习资源与复习策略

### 练习与错题
- 题库练习：内置 408 各章节选择题
- **错题本**：自动收录答错题目，支持标注笔记、标记已掌握、删除

### 数据统计
- 总学习时长、已完成章节数、错题数量
- 各科进度柱状图、学习时间分布、章节完成率等可视化图表

## 项目结构

```
ai-learning-system-1/
├── src/main/java/com/ailearning/   # 后端源码
│   ├── controller/                 # REST API 接口
│   ├── service/                    # 业务逻辑（含 AiService）
│   ├── mapper/                     # MyBatis 数据访问
│   └── entity/                     # 数据实体
├── src/main/resources/
│   ├── application.yml             # 应用配置
│   ├── schema.sql                  # 数据库初始化脚本
│   └── mapper/                     # MyBatis XML 映射
├── frontend/                       # Vue 3 前端
│   ├── src/views/                  # 页面组件
│   ├── src/api/                    # API 封装
│   └── vite.config.js              # 开发代理配置
└── uploads/                        # 用户上传文件目录
```

## 环境要求

- **JDK 17+**
- **Maven 3.6+**
- **Node.js 18+**（前端开发）
- **MySQL 8.0+**
- **DeepSeek API Key**（AI 功能所需）

## 快速启动

### 1. 准备数据库

在 MySQL 中创建数据库：

```sql
CREATE DATABASE ai_learning DEFAULT CHARACTER SET utf8mb4;
```

应用启动时会自动执行 `schema.sql` 初始化表结构与示例数据。

### 2. 配置后端

编辑 `src/main/resources/application.yml`，修改数据库连接与 AI 配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_learning?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password   # 改为你的 MySQL 密码

ai:
  api-key: your_deepseek_api_key   # 改为你的 DeepSeek API Key
  api-url: https://api.deepseek.com/v1/chat/completions
  model: deepseek-chat
```

### 3. 启动后端

在项目根目录执行：

```bash
mvn spring-boot:run
```

后端默认运行在 **http://localhost:8080**。

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端默认运行在 **http://localhost:5173**，开发模式下通过 Vite 代理将 `/api` 和 `/uploads` 请求转发至后端。

### 5. 访问系统

浏览器打开 [http://localhost:5173](http://localhost:5173)，使用以下测试账号登录：

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin  | 123456 | 管理员 |
| test   | 123456 | 普通用户 |

## 生产构建

```bash
# 前端打包
cd frontend
npm run build

# 后端打包
cd ..
mvn clean package
java -jar target/ai-learning-system-1.0.0.jar
```

## 主要 API 接口

| 模块 | 路径前缀 | 说明 |
|------|----------|------|
| 用户 | `/api/user` | 登录、注册、个人信息 |
| 章节 | `/api/chapter` | 408 知识点章节 |
| 进度 | `/api/progress` | 学习进度管理 |
| 资源 | `/api/resource` | 学习资源 |
| 题目 | `/api/question` | 练习题库 |
| 错题 | `/api/wrong-question` | 错题本 |
| AI | `/api/ai` | 问答、出题、推荐 |
| 聊天 | `/api/chat` | 对话历史 |
| 文件 | `/api/file` | 头像等文件上传 |

## 注意事项

- AI 相关功能依赖 DeepSeek API，请确保 `api-key` 配置正确且网络可访问
- 数据库密码、JWT 密钥、API Key 等敏感信息请勿提交至版本控制
- 首次启动前需确保 MySQL 服务已运行且 `ai_learning` 数据库已创建
