-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'user',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 章节表（408考研知识点）
CREATE TABLE IF NOT EXISTS chapter (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    subject VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    difficulty INTEGER DEFAULT 1,
    sort_order INTEGER DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 学习进度表
CREATE TABLE IF NOT EXISTS progress (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    chapter_id INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'not_started',
    score INTEGER DEFAULT 0,
    study_time INTEGER DEFAULT 0,
    last_study_time DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);

-- 学习资源表
CREATE TABLE IF NOT EXISTS resource (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    chapter_id INTEGER,
    title VARCHAR(200) NOT NULL,
    type VARCHAR(20) NOT NULL,
    url VARCHAR(500),
    description TEXT,
    difficulty INTEGER DEFAULT 1,
    view_count INTEGER DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);

-- 练习题表
CREATE TABLE IF NOT EXISTS question (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    chapter_id INTEGER,
    type VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    options TEXT,
    answer TEXT NOT NULL,
    analysis TEXT,
    difficulty INTEGER DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);

-- 错题本表
CREATE TABLE IF NOT EXISTS wrong_question (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    question_id INTEGER NOT NULL,
    user_answer TEXT,
    wrong_count INTEGER DEFAULT 1,
    last_wrong_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'wrong',
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (question_id) REFERENCES question(id)
);

-- 聊天历史表
CREATE TABLE IF NOT EXISTS chat_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 插入初始章节数据（408考研四门科目）
INSERT OR IGNORE INTO chapter (subject, name, description, difficulty, sort_order) VALUES
('数据结构', '线性表', '线性表的基本概念和实现', 2, 1),
('数据结构', '栈、队列和数组', '栈、队列和数组的基本概念和应用', 2, 2),
('数据结构', '树与二叉树', '树的基本概念和二叉树的性质', 3, 3),
('数据结构', '图', '图的基本概念和存储结构', 3, 4),
('数据结构', '查找', '顺序查找、折半查找和散列查找', 2, 5),
('数据结构', '排序', '各种排序算法的比较和应用', 3, 6),
('计算机组成原理', '计算机系统概述', '计算机的基本组成和性能指标', 1, 7),
('计算机组成原理', '数据的表示和运算', '数据的表示方法和运算方法', 2, 8),
('计算机组成原理', '存储系统', '存储器的层次结构', 3, 9),
('计算机组成原理', '指令系统', '指令的格式和寻址方式', 2, 10),
('计算机组成原理', '中央处理器', 'CPU的功能和结构', 3, 11),
('计算机组成原理', '总线', '总线的基本概念和分类', 2, 12),
('计算机组成原理', '输入输出系统', 'I/O接口和I/O方式', 2, 13),
('操作系统', '操作系统概述', '操作系统的基本概念和发展', 1, 14),
('操作系统', '进程管理', '进程的概念、状态转换和调度', 3, 15),
('操作系统', '内存管理', '连续分配和非连续分配', 3, 16),
('操作系统', '文件管理', '文件的逻辑结构和物理结构', 2, 17),
('操作系统', '设备管理', 'I/O软件和设备分配', 2, 18),
('计算机网络', '计算机网络概述', '计算机网络的定义和分类', 1, 19),
('计算机网络', '物理层', '数据通信基础和传输介质', 2, 20),
('计算机网络', '数据链路层', '数据链路层的功能和协议', 3, 21),
('计算机网络', '网络层', '网络层的功能和协议', 3, 22),
('计算机网络', '传输层', '传输层的功能和协议', 3, 23),
('计算机网络', '应用层', '应用层的功能和协议', 2, 24);

-- 插入测试用户
INSERT OR IGNORE INTO user (username, password, nickname, role) VALUES
('admin', '123456', '管理员', 'admin'),
('test', '123456', '测试用户', 'user');
