# ⚠️ 数据库表不存在 - 需要执行初始化脚本

## 🔍 问题分析

项目已经**成功启动**！✅  
看到这行日志说明项目运行正常：
```
Started GuidingdogDashboardApplication in 1.268 seconds
Tomcat started on port 8081
```

但是出现错误：
```
Table 'guidingdog_dashboard.robot_dog_data' doesn't exist
```

**原因**：数据库表还没有创建！

---

## 🔧 解决方法（必须执行）

### 方法一：使用 MySQL 命令行（推荐⭐⭐⭐⭐⭐）

1. **打开命令提示符**（CMD）

2. **登录 MySQL**
   ```bash
   mysql -u root -p
   ```
   输入你的MySQL密码（application.yml中配置的密码）

3. **执行初始化脚本**
   ```sql
   source D:/STUDY/backend_study/Guidingdog_Dashboard/Guidingdog_Dashboard/src/main/resources/database_init.sql
   ```

4. **验证表是否创建成功**
   ```sql
   USE GuidingDog_Dashboard;
   SHOW TABLES;
   ```
   
   应该看到三个表：
   ```
   +-------------------------------------+
   | Tables_in_guidingdog_dashboard      |
   +-------------------------------------+
   | camera_data                         |
   | radar_data                          |
   | robot_dog_data                      |
   +-------------------------------------+
   ```

---

### 方法二：使用 MySQL Workbench 或其他图形化工具

1. **打开 MySQL Workbench**

2. **连接到数据库**

3. **打开 SQL 脚本文件**
   - 文件位置：`D:\STUDY\backend_study\Guidingdog_Dashboard\Guidingdog_Dashboard\src\main\resources\database_init.sql`

4. **执行整个脚本**
   - 点击 ⚡ 闪电图标执行

5. **刷新查看**
   - 在左侧的 Schemas 中应该能看到 `GuidingDog_Dashboard` 数据库和3个表

---

### 方法三：手动复制粘贴 SQL

如果上面的方法都不行，直接在MySQL中执行以下SQL：

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS GuidingDog_Dashboard DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE GuidingDog_Dashboard;

-- 机器狗实时数据表
CREATE TABLE IF NOT EXISTS robot_dog_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    dog_id VARCHAR(50) NOT NULL COMMENT '机器狗ID',
    dog_name VARCHAR(100) COMMENT '机器狗名称',
    
    -- 速度数据
    velocity_x DOUBLE COMMENT 'X轴速度(m/s)',
    velocity_y DOUBLE COMMENT 'Y轴速度(m/s)',
    velocity_z DOUBLE COMMENT 'Z轴速度(m/s)',
    
    -- 加速度数据
    acceleration_x DOUBLE COMMENT 'X轴加速度(m/s²)',
    acceleration_y DOUBLE COMMENT 'Y轴加速度(m/s²)',
    acceleration_z DOUBLE COMMENT 'Z轴加速度(m/s²)',
    
    -- 姿态数据
    yaw DOUBLE COMMENT '偏航角/方位角(度)',
    pitch DOUBLE COMMENT '俯仰角(度)',
    roll DOUBLE COMMENT '横滚角(度)',
    
    -- 位置数据
    position_x DOUBLE COMMENT 'X坐标',
    position_y DOUBLE COMMENT 'Y坐标',
    position_z DOUBLE COMMENT 'Z坐标',
    
    -- 状态数据
    status VARCHAR(20) COMMENT '运行状态: IDLE, WALKING, RUNNING, STOPPED',
    battery_level DOUBLE COMMENT '电池电量(%)',
    temperature DOUBLE COMMENT '温度(℃)',
    
    -- 时间戳
    record_time DATETIME COMMENT '记录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_dog_id (dog_id),
    INDEX idx_record_time (record_time),
    INDEX idx_dog_record (dog_id, record_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机器狗实时数据表';

-- 摄像头数据表
CREATE TABLE IF NOT EXISTS camera_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    dog_id VARCHAR(50) NOT NULL COMMENT '机器狗ID',
    camera_id VARCHAR(20) NOT NULL COMMENT '摄像头ID: front, back, left, right',
    image_url TEXT COMMENT '图像URL或Base64数据',
    width INT COMMENT '图像宽度',
    height INT COMMENT '图像高度',
    format VARCHAR(10) COMMENT '图像格式: jpg, png',
    capture_time DATETIME COMMENT '拍摄时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_dog_camera (dog_id, camera_id),
    INDEX idx_capture_time (capture_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄像头数据表';

-- 雷达数据表
CREATE TABLE IF NOT EXISTS radar_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    dog_id VARCHAR(50) NOT NULL COMMENT '机器狗ID',
    radar_id VARCHAR(20) COMMENT '雷达ID',
    scan_data TEXT COMMENT '雷达扫描点云数据(JSON)',
    point_count INT COMMENT '点云数量',
    
    -- 障碍物检测距离
    min_distance DOUBLE COMMENT '最近障碍物距离(m)',
    front_distance DOUBLE COMMENT '前方距离(m)',
    left_distance DOUBLE COMMENT '左侧距离(m)',
    right_distance DOUBLE COMMENT '右侧距离(m)',
    back_distance DOUBLE COMMENT '后方距离(m)',
    
    scan_time DATETIME COMMENT '扫描时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_dog_id (dog_id),
    INDEX idx_scan_time (scan_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='雷达数据表';

-- 插入测试数据
INSERT INTO robot_dog_data (dog_id, dog_name, velocity_x, velocity_y, velocity_z, 
                            acceleration_x, acceleration_y, acceleration_z,
                            yaw, pitch, roll,
                            position_x, position_y, position_z,
                            status, battery_level, temperature, record_time)
VALUES ('DOG001', 'UnitreeGo1', 0.5, 0.2, 0.0,
        0.1, 0.05, 0.0,
        45.0, 0.0, 0.0,
        10.5, 20.3, 0.0,
        'WALKING', 85.5, 35.2, NOW());

INSERT INTO radar_data (dog_id, radar_id, point_count, 
                        min_distance, front_distance, left_distance, right_distance, back_distance,
                        scan_time)
VALUES ('DOG001', 'RADAR01', 1000, 2.5, 3.0, 4.5, 5.0, 6.0, NOW());
```

---

## ✅ 执行完成后

1. **无需重启项目**
   - 数据模拟器会自动继续尝试插入数据
   - 稍等几秒，错误日志会消失

2. **打开浏览器访问**
   ```
   http://localhost:8081
   ```

3. **你将看到**
   - 🎨 精美的紫色渐变仪表盘
   - 🟢 右上角"已连接"
   - 📊 数据每秒自动更新！

---

## 📊 验证数据是否正常

在MySQL中查询：
```sql
USE GuidingDog_Dashboard;

-- 查看最新数据
SELECT * FROM robot_dog_data ORDER BY record_time DESC LIMIT 5;

-- 查看数据总数（应该不断增加）
SELECT COUNT(*) FROM robot_dog_data;
```

数据会每秒增加一条！

---

## 🎯 快速总结

**你只需要做一件事**：

1. 打开 MySQL
2. 执行 `database_init.sql` 脚本
3. 刷新浏览器 http://localhost:8081

**就这么简单！** ✅

---

**现在立即去执行数据库初始化吧！** 🚀

