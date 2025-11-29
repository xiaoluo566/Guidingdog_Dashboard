# 🚀 快速启动指南

## 第一步：初始化数据库

### 1. 启动 MySQL 服务

确保 MySQL 服务正在运行。

### 2. 创建数据库并初始化表

**方法一：使用命令行**
```bash
# 登录 MySQL
mysql -u root -p

# 在 MySQL 中执行
source D:/STUDY/backend_study/Guidingdog_Dashboard/Guidingdog_Dashboard/src/main/resources/database_init.sql
```

**方法二：使用 MySQL Workbench 或其他客户端**
1. 打开 `src/main/resources/database_init.sql` 文件
2. 复制全部内容
3. 在 MySQL 客户端中执行

### 3. 验证数据库

```sql
-- 查看数据库
SHOW DATABASES;

-- 使用数据库
USE GuidingDog_Dashboard;

-- 查看表
SHOW TABLES;

-- 应该看到三个表：
-- robot_dog_data
-- camera_data
-- radar_data

-- 查看测试数据
SELECT * FROM robot_dog_data;
```

---

## 第二步：配置数据库连接

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/GuidingDog_Dashboard
    username: root
    password: 你的MySQL密码  # 修改这里！
    driver-class-name: com.mysql.cj.jdbc.Driver
```

---

## 第三步：启动项目

### 方法一：使用 IDE（推荐）

1. 在 IntelliJ IDEA 中打开项目
2. 找到 `GuidingdogDashboardApplication.java`
3. 右键 → Run 'GuidingdogDashboardApplication'

### 方法二：使用命令行

在项目根目录下执行：

```bash
# Windows
mvnw.cmd spring-boot:run

# 或者先打包再运行
mvnw.cmd clean package
java -jar target/Guidingdog_Dashboard-0.0.1-SNAPSHOT.jar
```

### 方法三：双击启动脚本

双击 `start.bat` 文件

---

## 第四步：访问仪表盘

启动成功后，你会看到类似的日志：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

Started GuidingdogDashboardApplication in 3.456 seconds
```

然后在浏览器中打开：

```
http://localhost:8080
```

---

## 第五步：查看效果

### 仪表盘界面

你将看到一个漂亮的紫色渐变背景的仪表盘，包含：

- ✅ **连接状态指示器**（右上角）
- ✅ **基本信息卡片**：机器狗ID、名称、状态、电池、温度
- ✅ **速度信息卡片**：X/Y/Z轴速度和合成速度
- ✅ **加速度信息卡片**：三轴加速度
- ✅ **姿态信息卡片**：方位角、俯仰角、横滚角
- ✅ **位置信息卡片**：三维坐标
- ✅ **雷达距离卡片**：5个方向的障碍物距离
- ✅ **摄像头视图区域**：预留的4个摄像头显示区

### 数据自动更新

由于项目内置了**数据模拟器**，启动后：
- 🔄 每秒自动生成新数据
- 📡 通过 WebSocket 推送到前端
- 💾 同时保存到 MySQL 数据库
- 🎯 页面数据实时更新

你会看到：
- 速度值不断变化
- 位置坐标逐渐移动
- 方位角旋转
- 状态在 IDLE/WALKING/RUNNING/STOPPED 之间切换
- 电池和温度小幅波动

---

## 验证功能

### 1. 检查 WebSocket 连接

- 右上角应该显示绿色的 **"已连接"**
- 数据应该每秒更新一次

### 2. 检查数据库

```sql
-- 查看最新的机器狗数据
SELECT * FROM robot_dog_data ORDER BY record_time DESC LIMIT 10;

-- 查看雷达数据
SELECT * FROM radar_data ORDER BY scan_time DESC LIMIT 10;

-- 查看记录总数
SELECT COUNT(*) FROM robot_dog_data;
```

数据会不断增加！

### 3. 测试 REST API

打开新的浏览器标签页，访问：

```
http://localhost:8080/api/robot/health
```

应该看到：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "服务运行正常"
}
```

获取实时数据：
```
http://localhost:8080/api/robot/data/realtime/DOG001
```

应该看到完整的JSON数据。

---

## 🎯 常见问题排查

### 问题1：端口被占用

**错误信息**：
```
Port 8080 was already in use
```

**解决方法**：
修改 `application.yml`：
```yaml
server:
  port: 8081  # 改成其他端口
```

然后访问 `http://localhost:8081`

---

### 问题2：数据库连接失败

**错误信息**：
```
Failed to configure a DataSource
Communications link failure
```

**排查步骤**：

1. ✅ 确认 MySQL 服务已启动
   ```bash
   # Windows
   net start MySQL80
   ```

2. ✅ 确认数据库已创建
   ```sql
   SHOW DATABASES LIKE 'GuidingDog_Dashboard';
   ```

3. ✅ 确认用户名密码正确
   - 检查 `application.yml` 中的配置

4. ✅ 确认 MySQL 端口（默认3306）

---

### 问题3：页面打不开

**可能原因**：

1. 项目没有启动成功
   - 查看控制台日志
   - 确认看到 "Started GuidingdogDashboardApplication"

2. 浏览器缓存问题
   - 按 Ctrl+F5 强制刷新

3. 防火墙阻止
   - 临时关闭防火墙测试

---

### 问题4：WebSocket 连接失败

**表现**：右上角显示红色 "未连接"

**排查**：

1. 打开浏览器控制台（F12）
2. 查看 Console 标签中的错误信息
3. 常见原因：
   - 端口不对（如果改了端口，需要修改 index.html 中的 WS_URL）
   - 后端未启动
   - 防火墙阻止

---

### 问题5：没有数据显示

**排查**：

1. ✅ 确认 WebSocket 已连接（绿色）
2. ✅ 等待几秒（数据每秒更新一次）
3. ✅ 查看后端日志，应该看到：
   ```
   保存机器狗数据: dogId=DOG001, status=WALKING
   保存雷达数据: dogId=DOG001, minDistance=2.5
   ```

4. ✅ 如果没有日志，检查 DataSimulator 是否已启动

---

## 🎓 下一步

### 1. 研究代码结构
- 从 Controller 开始，理解请求处理流程
- 学习 Service 层的业务逻辑
- 理解 Mapper 的数据库操作
- 研究 WebSocket 的实时推送机制

### 2. 修改前端界面
- 调整 `index.html` 中的样式
- 添加图表库可视化数据
- 实现摄像头图像显示

### 3. 连接真实机器狗
- 参考 `TEST_EXAMPLES.md` 中的示例
- 从宇树SDK获取真实数据
- 调用 API 发送到仪表盘

### 4. 扩展功能
- 添加历史数据图表
- 实现多机器狗切换
- 添加报警功能
- 实现远程控制

---

## 📚 重要文件位置

```
项目根目录/
├── src/main/resources/application.yml          # 配置文件
├── src/main/resources/database_init.sql        # 数据库脚本
├── src/main/resources/static/index.html        # 前端页面
├── src/main/java/.../GuidingdogDashboardApplication.java  # 启动类
├── README.md                                   # 完整文档
├── PROJECT_SUMMARY.md                          # 项目总结
├── TEST_EXAMPLES.md                            # 测试示例
└── start.bat                                   # 启动脚本
```

---

## ✅ 完成检查清单

启动成功后，确认以下各项：

- [ ] MySQL 服务已启动
- [ ] 数据库 GuidingDog_Dashboard 已创建
- [ ] 三个表（robot_dog_data, camera_data, radar_data）已创建
- [ ] application.yml 中的密码已修改
- [ ] Spring Boot 应用已成功启动
- [ ] 浏览器可以打开 http://localhost:8080
- [ ] 页面右上角显示"已连接"（绿色）
- [ ] 数据每秒自动更新
- [ ] 可以访问 API（如 /api/robot/health）

全部完成，恭喜你！🎉

---

## 🆘 需要帮助？

如果遇到问题：

1. 📖 查看 `README.md` 完整文档
2. 📝 查看 `PROJECT_SUMMARY.md` 项目总结
3. 🧪 查看 `TEST_EXAMPLES.md` 测试示例
4. 🔍 查看控制台日志输出
5. 🔍 查看浏览器控制台（F12）

---

**祝你使用愉快！🚀**

