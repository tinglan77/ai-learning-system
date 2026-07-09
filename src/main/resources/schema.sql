-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'user',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 章节表（408考研知识点）
CREATE TABLE IF NOT EXISTS `chapter` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subject VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    difficulty INT DEFAULT 1,
    sort_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_subject_name (subject, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学习进度表
CREATE TABLE IF NOT EXISTS `progress` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    chapter_id INT NOT NULL,
    status VARCHAR(20) DEFAULT 'not_started',
    score INT DEFAULT 0,
    study_time INT DEFAULT 0,
    last_study_time DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_chapter (user_id, chapter_id),
    FOREIGN KEY (user_id) REFERENCES `user`(id),
    FOREIGN KEY (chapter_id) REFERENCES `chapter`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学习资源表
CREATE TABLE IF NOT EXISTS `resource` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    chapter_id INT,
    title VARCHAR(200) NOT NULL,
    type VARCHAR(20) NOT NULL,
    url VARCHAR(500),
    description TEXT,
    difficulty INT DEFAULT 1,
    view_count INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chapter_id) REFERENCES `chapter`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 练习题表
CREATE TABLE IF NOT EXISTS `question` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    chapter_id INT,
    type VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    options TEXT,
    answer TEXT NOT NULL,
    analysis TEXT,
    difficulty INT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_chapter_content (chapter_id, content(100)),
    FOREIGN KEY (chapter_id) REFERENCES `chapter`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 错题本表
CREATE TABLE IF NOT EXISTS `wrong_question` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    question_id INT NOT NULL,
    user_answer TEXT,
    wrong_count INT DEFAULT 1,
    last_wrong_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'wrong',
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_question (user_id, question_id),
    FOREIGN KEY (user_id) REFERENCES `user`(id),
    FOREIGN KEY (question_id) REFERENCES `question`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 聊天历史表
CREATE TABLE IF NOT EXISTS `chat_history` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入初始章节数据（408考研四门科目）
INSERT IGNORE INTO `chapter` (subject, name, description, difficulty, sort_order) VALUES
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
INSERT IGNORE INTO `user` (username, password, nickname, role) VALUES
('admin', '123456', '管理员', 'admin'),
('test', '123456', '测试用户', 'user');

-- 插入学习资源数据
INSERT IGNORE INTO `resource` (chapter_id, title, type, url, description, difficulty, view_count) VALUES
(1, '线性表基本概念', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '线性表的定义、存储结构和基本操作', 2, 120),
(1, '单链表实现详解', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '单链表的创建、插入、删除操作演示', 3, 89),
(2, '栈的基本操作', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '栈的定义、入栈、出栈操作', 2, 156),
(2, '队列的应用', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '循环队列和链队列的实现', 2, 78),
(3, '二叉树遍历详解', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '前序、中序、后序遍历算法', 3, 234),
(3, '平衡二叉树', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', 'AVL树的旋转操作和平衡调整', 4, 67),
(4, '图的存储结构', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '邻接矩阵和邻接表', 3, 145),
(4, '最短路径算法', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', 'Dijkstra和Floyd算法详解', 4, 198),
(5, '折半查找', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '折半查找的原理和实现', 2, 112),
(5, '哈希表', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '哈希函数的构造和冲突处理', 3, 167),
(6, '快速排序', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '快速排序的原理和代码实现', 3, 289),
(6, '归并排序', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '归并排序的分治思想', 3, 134),
(7, '计算机性能指标', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '吞吐量、响应时间和带宽', 1, 95),
(8, 'IEEE754浮点数', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '浮点数的表示和运算', 3, 178),
(9, 'Cache映射方式', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '直接映射、全相联和组相联', 4, 201),
(10, '指令寻址方式', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '立即寻址、直接寻址、间接寻址', 2, 88),
(11, 'CPU流水线', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '指令流水线的原理和冒险', 4, 156),
(14, '操作系统发展', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '批处理、分时和实时系统', 1, 76),
(15, '进程调度算法', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', 'FCFS、SJF、优先级调度', 3, 245),
(15, '进程同步与互斥', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '信号量和PV操作', 4, 312),
(16, '虚拟内存', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '页面置换算法详解', 4, 267),
(17, '文件分配方式', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '连续、链接和索引分配', 3, 98),
(19, 'OSI参考模型', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', '七层模型和各层功能', 2, 189),
(21, 'CSMA/CD协议', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', '以太网的载波监听多路访问', 3, 134),
(22, 'IP地址与子网划分', 'video', 'https://www.bilibili.com/video/BV1nJ411V7bd', 'IP地址分类和CIDR', 3, 278),
(23, 'TCP三次握手', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', 'TCP连接建立和释放过程', 3, 356),
(24, 'HTTP与HTTPS', 'article', 'https://www.bilibili.com/video/BV1nJ411V7bd', 'HTTP协议和SSL/TLS加密', 2, 201);

-- 插入练习题数据
INSERT IGNORE INTO `question` (chapter_id, type, content, options, answer, analysis, difficulty) VALUES
(1, 'choice', '线性表的顺序存储结构中，第i个元素的存储地址计算公式为？', 'A. LOC(a1)+(i-1)*size\nB. LOC(a1)+i*size\nC. LOC(a1)+(i+1)*size\nD. LOC(a1)+i', 'A', '顺序存储结构中，元素连续存放，第i个元素的地址 = 首地址 + (i-1) * 每个元素的大小。', 2),
(1, 'choice', '在单链表中，删除节点p的后继节点，需要修改几个指针？', 'A. 0个\nB. 1个\nC. 2个\nD. 3个', 'B', '只需将p的next指针指向p后继的后继即可：p->next = p->next->next，修改1个指针。', 2),
(2, 'choice', '栈的特性是？', 'A. 先进先出\nB. 后进先出\nC. 随机存取\nD. 顺序存取', 'B', '栈是后进先出（LIFO）的线性表，只能在栈顶进行插入和删除操作。', 1),
(3, 'choice', '一棵完全二叉树有1001个结点，其中叶子结点的个数是？', 'A. 250\nB. 500\nC. 501\nD. 505', 'C', '完全二叉树中，叶子结点数 = ⌈n/2⌉ = ⌈1001/2 = 501。', 3),
(3, 'choice', '二叉树的前序遍历序列和中序遍历序列相同，则该二叉树？', 'A. 只有一个根结点\nB. 每个结点都没有左子树\nC. 每个结点都没有右子树\nD. 是一棵满二叉树', 'B', '前序遍历先访问根再访问左子树再访问右子树，中序遍历先访问左子树再访问根再访问右子树。若两者相同，说明没有左子树。', 3),
(4, 'choice', '用邻接矩阵存储有向图，第i行的非零元素个数表示？', 'A. 顶点i的入度\nB. 顶点i的出度\nC. 顶点i的度\nD. 与顶点i相邻的顶点数', 'B', '邻接矩阵中第i行表示从顶点i出发的边，非零元素个数即为顶点i的出度。', 3),
(5, 'choice', '对长度为n的有序表进行折半查找，最坏情况下的比较次数为？', 'A. n\nB. n/2\nC. log2(n)+1\nD. log2(n)', 'C', '折半查找每次将查找范围缩小一半，最坏比较次数为log2(n)⌋+1。', 2),
(6, 'choice', '下列排序算法中，平均时间复杂度为O(nlogn)的是？', 'A. 冒泡排序\nB. 插入排序\nC. 快速排序\nD. 选择排序', 'C', '快速排序平均时间复杂度为O(nlogn)，冒泡、插入、选择排序均为O(n²)。', 2),
(9, 'choice', 'Cache采用直接映射方式，主存地址划分为？', 'A. 标记+块内地址\nB. 标记+组号+块内地址\nC. 标记+行号+块内地址\nD. 区号+块号+块内地址', 'C', '直接映射将主存地址分为标记（Tag）、行号（Line/Cache行索引）和块内地址（Offset）三部分。', 3),
(11, 'choice', 'CPU中程序计数器PC的作用是？', 'A. 保存当前正在执行的指令\nB. 保存下一条要执行的指令地址\nC. 保存运算结果\nD. 保存指令的操作码', 'B', '程序计数器（PC）保存下一条要取出执行的指令的地址，每取一条指令后PC自动加1。', 2),
(15, 'choice', '下列进程调度算法中，可能导致饥饿现象的是？', 'A. 先来先服务\nB. 短作业优先\nC. 时间片轮转\nD. 多级反馈队列', 'B', '短作业优先（SJF）算法中，长作业可能因为不断有短作业到达而长时间得不到执行，产生饥饿现象。', 3),
(15, 'choice', '信号量S的初值为3，当前值为-1，则等待队列中有几个进程？', 'A. 0\nB. 1\nC. 2\nD. 3', 'B', '信号量值为负数时，其绝对值表示等待队列中的进程数。|-1|=1，所以有1个进程在等待。', 3),
(16, 'choice', 'LRU页面置换算法的依据是？', 'A. 最近最少使用的页面\nB. 最早进入内存的页面\nC. 访问频率最低的页面\nD. 随机选择页面', 'A', 'LRU（Least Recently Used）算法选择最近最久未使用的页面进行置换，基于局部性原理。', 3),
(22, 'choice', 'IP地址192.168.1.0/24的子网掩码是？', 'A. 255.255.255.0\nB. 255.255.0.0\nC. 255.0.0.0\nD. 255.255.255.128', 'A', '/24表示前24位为网络位，子网掩码为255.255.255.0。', 2),
(23, 'choice', 'TCP三次握手的正确顺序是？', 'A. SYN→SYN+ACK→ACK\nB. ACK→SYN→SYN+ACK\nC. SYN→ACK→SYN+ACK\nD. SYN+ACK→SYN→ACK', 'A', 'TCP三次握手：客户端发SYN→服务端回SYN+ACK→客户端发ACK，完成连接建立。', 3);

-- 插入学习进度数据（test用户的学习记录）
INSERT IGNORE INTO `progress` (user_id, chapter_id, status, score, study_time, last_study_time) VALUES
(2, 1, 'completed', 85, 120, '2025-07-01 10:30:00'),
(2, 2, 'completed', 90, 90, '2025-07-02 14:00:00'),
(2, 3, 'in_progress', 60, 45, '2025-07-03 09:15:00'),
(2, 4, 'in_progress', 40, 30, '2025-07-03 16:20:00'),
(2, 5, 'not_started', 0, 0, NULL),
(2, 6, 'not_started', 0, 0, NULL),
(2, 7, 'completed', 95, 60, '2025-07-04 11:00:00'),
(2, 8, 'in_progress', 55, 40, '2025-07-04 15:30:00'),
(2, 9, 'not_started', 0, 0, NULL),
(2, 10, 'completed', 88, 75, '2025-07-05 10:00:00'),
(2, 14, 'completed', 92, 50, '2025-07-05 14:00:00'),
(2, 15, 'in_progress', 50, 35, '2025-07-06 09:00:00'),
(2, 19, 'completed', 80, 65, '2025-07-06 11:30:00'),
(2, 22, 'in_progress', 45, 25, '2025-07-06 16:00:00');

-- 插入错题数据（test用户的错题）
INSERT IGNORE INTO `wrong_question` (user_id, question_id, user_answer, wrong_count, status, note, last_wrong_time) VALUES
(2, 4, 'B', 2, 'wrong', '完全二叉树叶子节点计算容易出错，需要记住公式', '2025-07-03 10:00:00'),
(2, 11, 'A', 1, 'wrong', '饥饿现象的理解需要加强', '2025-07-04 14:30:00'),
(2, 13, 'B', 1, 'wrong', 'LRU和FIFO的区别要搞清楚', '2025-07-05 09:00:00'),
(2, 15, 'C', 3, 'wrong', 'TCP三次握手顺序反复出错，重点记忆', '2025-07-06 11:00:00');
