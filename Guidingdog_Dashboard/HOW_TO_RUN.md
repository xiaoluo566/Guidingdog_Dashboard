# 🔧 问题已解决 - 如何正确运行项目

## ✅ 已修复的问题

**1. WebSocketConfig.java 编译错误** - 已解决！
- 原因：`ServerEndpointExporter` 依赖没有正确加载
- 解决：使用Spring Boot自动配置

**2. 端口8080被占用** - 已解决！
- 原因：端口8080可能被其他程序占用
- 解决：已将端口修改为 **8081**
- 访问地址：**http://localhost:8081**

---

## 🚀 正确的启动方式

### 方式一：使用 IntelliJ IDEA（推荐⭐⭐⭐⭐⭐）

1. **在IDE中找到主类**
   ```
   src/main/java/com/nixiak/guidingdog_dashboard/GuidingdogDashboardApplication.java
   ```

2. **右键点击文件**

3. **选择**：`Run 'GuidingdogDashboardApplication'`

4. **等待启动**，看到以下日志表示成功：
   ```
   Started GuidingdogDashboardApplication in X.XXX seconds
   ```

5. **打开浏览器**：http://localhost:8081 ⭐

---

### 方式二：使用Maven命令（需要先安装Maven）

如果你安装了Maven：

```bash
cd D:\STUDY\backend_study\Guidingdog_Dashboard\Guidingdog_Dashboard
mvn spring-boot:run
```

---

### 方式三：先编译打包，再运行JAR

```bash
# 1. 编译打包
cd D:\STUDY\backend_study\Guidingdog_Dashboard\Guidingdog_Dashboard
mvn clean package -DskipTests

# 2. 运行JAR
java -jar target/Guidingdog_Dashboard-0.0.1-SNAPSHOT.jar
```

---

## ⚠️ 启动前必须检查

### 1. 数据库必须已启动
```bash
# 检查MySQL服务是否运行
net start | findstr MySQL
```

如果没有运行，启动MySQL：
```bash
net start MySQL80
```

### 2. 数据库必须已创建
在MySQL中执行：
```sql
-- 查看数据库是否存在
SHOW DATABASES LIKE 'GuidingDog_Dashboard';

-- 如果不存在，执行初始化脚本
source D:/STUDY/backend_study/Guidingdog_Dashboard/Guidingdog_Dashboard/src/main/resources/database_init.sql
```

### 3. 数据库密码必须正确
检查 `application.yml` 中的密码是否正确：
```yaml
spring:
  datasource:
    username: root
    password: 123456  # 改成你的MySQL密码！
```

---

## 📋 启动步骤清单

- [ ] ✅ MySQL服务已启动
- [ ] ✅ 数据库 GuidingDog_Dashboard 已创建
- [ ] ✅ application.yml 中的密码已修改
- [ ] ✅ IDE已加载项目
- [ ] ✅ Maven依赖已下载完成
- [ ] ✅ 编译没有错误

全部完成后，使用 **方式一** 在IDE中运行！

---

## 🎯 常见启动错误及解决

### 错误1：端口被占用
```
Port 8080 was already in use
```

**解决**：修改端口
```yaml
# application.yml 添加
server:
  port: 8081
```

---

### 错误2：数据库连接失败
```
Failed to configure a DataSource
Communications link failure
```

**解决步骤**：
1. 确认MySQL已启动
2. 确认数据库已创建
3. 确认用户名密码正确
4. 确认端口是3306

---

### 错误3：找不到表
```
Table 'guidingdog_dashboard.robot_dog_data' doesn't exist
```

**解决**：执行数据库初始化脚本
```sql
source D:/STUDY/backend_study/Guidingdog_Dashboard/Guidingdog_Dashboard/src/main/resources/database_init.sql
```

---

## ✅ 成功启动的标志

看到以下日志表示启动成功：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

:: Spring Boot ::               (v3.5.8)

2025-11-28 ... : Starting GuidingdogDashboardApplication
2025-11-28 ... : Started GuidingdogDashboardApplication in 3.456 seconds
2025-11-28 ... : 启动数据模拟器...
```

然后：
1. 浏览器访问：http://localhost:8081 ⭐
2. 看到精美的仪表盘页面
3. 右上角显示 🟢 "已连接"
4. 数据每秒自动更新

---

## 🎉 启动成功后

### 查看效果
- **仪表盘**: http://localhost:8081 ⭐
- **健康检查**: http://localhost:8081/api/robot/health
- **实时数据**: http://localhost:8081/api/robot/data/realtime/DOG001

### 查看数据库
```sql
USE GuidingDog_Dashboard;
SELECT * FROM robot_dog_data ORDER BY record_time DESC LIMIT 10;
```

应该看到数据不断增加！

---

## 💡 为什么之前的命令行方式不工作？

PowerShell 对批处理文件（.cmd）的支持有限。最佳方式是：

1. **使用IDE运行** ✅ 最简单
2. **切换到CMD** - 如果必须用命令行：
   ```cmd
   cmd /c "cd /d D:\STUDY\backend_study\Guidingdog_Dashboard\Guidingdog_Dashboard && mvnw.cmd spring-boot:run"
   ```

但推荐使用IDE，这样可以：
- 看到完整的日志
- 方便调试
- 快速重启
- 查看内存使用

---

## 📞 还有问题？

1. 查看IDE底部的 **Run** 或 **Console** 窗口的错误日志
2. 检查上面的 "常见启动错误及解决"
3. 阅读 `QUICK_START.md` 的详细问题排查

---

**现在，请在IDE中运行项目！** 🚀

右键点击 `GuidingdogDashboardApplication.java` → **Run**

