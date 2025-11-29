# 导盲犬仪表盘 (Guiding Dog Dashboard)

一个基于 Spring Boot + MySQL + MyBatis 的导盲犬机器狗实时数据监控系统。

## 项目简介

本项目是一个针对宇树机器狗的实时数据监控仪表盘，可以实时显示：
- 📹 摄像头视频流
- 📡 雷达扫描数据
- 🚀 速度和加速度信息
- 🧭 方位角、俯仰角、横滚角等姿态数据
- 📍 位置坐标
- 🔋 电池电量和温度等状态信息

## 技术栈

- **后端框架**: Spring Boot 3.5.8
- **数据库**: MySQL 8.0+
- **ORM框架**: MyBatis 3.0.5
- **实时通信**: WebSocket
- **前端**: HTML5 + CSS3 + JavaScript (原生)
- **Java版本**: Java 25

## 项目结构

```
src/main/java/com/nixiak/guidingdog_dashboard/
├── GuidingdogDashboardApplication.java  # 主启动类
├── controller/                           # 控制器层
│   └── RobotDogController.java          # 机器狗数据API
├── service/                              # 业务逻辑层
│   └── RobotDogService.java             # 数据处理服务
├── mapper/                               # 数据访问层
│   ├── RobotDogDataMapper.java          # 机器狗数据
│   ├── CameraDataMapper.java            # 摄像头数据
│   └── RadarDataMapper.java             # 雷达数据
├── entity/                               # 实体类
│   ├── RobotDogData.java                # 机器狗数据实体
│   ├── CameraData.java                  # 摄像头数据实体
│   └── RadarData.java                   # 雷达数据实体
├── dto/                                  # 数据传输对象
│   ├── RealtimeDataDTO.java             # 实时数据DTO
│   └── Result.java                      # 统一响应结果
├── config/                               # 配置类
│   ├── WebConfig.java                   # Web配置（跨域）
│   └── WebSocketConfig.java             # WebSocket配置
├── websocket/                            # WebSocket服务
│   └── RealtimeDataWebSocket.java       # 实时数据推送
└── simulator/                            # 数据模拟器
    └── DataSimulator.java               # 测试数据生成器

src/main/resources/
├── application.yml                       # 应用配置
├── database_init.sql                     # 数据库初始化脚本
└── static/
    └── index.html                       # 前端仪表盘页面
```

## 快速开始

### 1. 环境准备

确保你已安装：
- Java 25 或以上
- MySQL 8.0 或以上
- Maven 3.6+

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source src/main/resources/database_init.sql
```

或者直接在MySQL客户端中执行 `database_init.sql` 文件。

### 3. 配置数据库连接

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/GuidingDog_Dashboard
    username: root
    password: 你的密码
```

### 4. 启动项目

```bash
# 使用Maven启动
mvn spring-boot:run

# 或者先打包再运行
mvn clean package
java -jar target/Guidingdog_Dashboard-0.0.1-SNAPSHOT.jar
```

### 5. 访问仪表盘

在浏览器中打开：
```
http://localhost:8080
```

## API 接口文档

### 1. 上传机器狗数据

**POST** `/api/robot/data/upload`

请求体示例：
```json
{
  "dogId": "DOG001",
  "dogName": "UnitreeGo1",
  "velocityX": 0.5,
  "velocityY": 0.2,
  "velocityZ": 0.0,
  "accelerationX": 0.1,
  "accelerationY": 0.05,
  "accelerationZ": 0.0,
  "yaw": 45.0,
  "pitch": 0.0,
  "roll": 0.0,
  "positionX": 10.5,
  "positionY": 20.3,
  "positionZ": 0.0,
  "status": "WALKING",
  "batteryLevel": 85.5,
  "temperature": 35.2,
  "recordTime": "2025-11-28T14:30:00"
}
```

### 2. 上传摄像头图像

**POST** `/api/robot/camera/upload`

请求体示例：
```json
{
  "dogId": "DOG001",
  "cameraId": "front",
  "imageUrl": "data:image/jpeg;base64,...",
  "width": 1920,
  "height": 1080,
  "format": "jpg",
  "captureTime": "2025-11-28T14:30:00"
}
```

### 3. 上传雷达数据

**POST** `/api/robot/radar/upload`

请求体示例：
```json
{
  "dogId": "DOG001",
  "radarId": "RADAR01",
  "scanData": "{...}",
  "pointCount": 1000,
  "minDistance": 2.5,
  "frontDistance": 3.0,
  "leftDistance": 4.5,
  "rightDistance": 5.0,
  "backDistance": 6.0,
  "scanTime": "2025-11-28T14:30:00"
}
```

### 4. 获取实时数据

**GET** `/api/robot/data/realtime/{dogId}`

响应示例：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "dogId": "DOG001",
    "dogName": "UnitreeGo1",
    "status": "WALKING",
    "velocityX": 0.5,
    "velocityY": 0.2,
    "velocityZ": 0.0,
    "speed": 0.54,
    "accelerationX": 0.1,
    "accelerationY": 0.05,
    "accelerationZ": 0.0,
    "yaw": 45.0,
    "pitch": 0.0,
    "roll": 0.0,
    "positionX": 10.5,
    "positionY": 20.3,
    "positionZ": 0.0,
    "batteryLevel": 85.5,
    "temperature": 35.2,
    "minDistance": 2.5,
    "frontDistance": 3.0,
    "leftDistance": 4.5,
    "rightDistance": 5.0,
    "backDistance": 6.0,
    "timestamp": 1732780200000
  }
}
```

### 5. 获取所有机器狗数据

**GET** `/api/robot/data/all`

### 6. 获取摄像头图像

**GET** `/api/robot/camera/{dogId}/{cameraId}`

### 7. 获取历史数据

**GET** `/api/robot/data/history/{dogId}?startTime=2025-11-28T00:00:00&endTime=2025-11-28T23:59:59&limit=100`

## WebSocket 实时推送

连接地址：
```
ws://localhost:8080/ws/realtime/{dogId}
```

前端示例：
```javascript
const websocket = new WebSocket('ws://localhost:8080/ws/realtime/DOG001');

websocket.onmessage = function(event) {
    const data = JSON.parse(event.data);
    console.log('收到实时数据:', data);
};
```

## 数据模拟器

项目内置了数据模拟器，启动后会自动每秒生成并推送模拟数据，方便测试。

如需关闭模拟器，可以在 `DataSimulator.java` 中注释掉 `@Component` 注解。

## 与宇树机器狗集成

### 方式一：直接调用API

在机器狗的控制程序中，通过HTTP请求定时发送数据到本系统：

```python
import requests
import json

def send_robot_data(data):
    url = "http://your-server:8080/api/robot/data/upload"
    headers = {'Content-Type': 'application/json'}
    response = requests.post(url, data=json.dumps(data), headers=headers)
    return response.json()
```

### 方式二：通过ROS话题订阅

如果机器狗使用ROS系统，可以订阅相关话题并转发到本系统。

## 数据库表结构

### robot_dog_data - 机器狗实时数据表
- 存储速度、加速度、姿态、位置、状态等信息
- 建议定期清理旧数据（保留30天）

### camera_data - 摄像头数据表
- 存储图像URL或Base64编码的图像数据
- 建议定期清理（保留7天）

### radar_data - 雷达数据表
- 存储雷达扫描的点云数据和障碍物距离
- 建议定期清理（保留30天）

## 性能优化建议

1. **数据库优化**
   - 定期清理历史数据
   - 为常用查询字段添加索引
   - 考虑使用时序数据库（如InfluxDB）存储历史数据

2. **WebSocket优化**
   - 控制推送频率（建议10-30Hz）
   - 压缩传输数据

3. **图像传输优化**
   - 使用JPEG压缩
   - 降低分辨率
   - 考虑使用视频流协议（RTSP/WebRTC）

## 常见问题

### 1. WebSocket连接失败
- 检查防火墙设置
- 确认端口8080未被占用
- 查看浏览器控制台错误信息

### 2. 数据库连接失败
- 确认MySQL服务已启动
- 检查用户名密码是否正确
- 确认数据库已创建

### 3. 页面无数据显示
- 检查WebSocket连接状态
- 确认数据模拟器已启动
- 查看后端日志

## 开发团队

- 开发者：nixiak
- 项目类型：学习项目
- 创建时间：2025-11

## 许可证

本项目仅用于学习和研究目的。

## 贡献

欢迎提交Issue和Pull Request！

## 未来计划

- [ ] 添加用户认证和授权
- [ ] 支持多机器狗同时监控
- [ ] 添加数据可视化图表
- [ ] 支持视频流实时播放
- [ ] 添加雷达点云3D可视化
- [ ] 支持远程控制机器狗
- [ ] 移动端适配
- [ ] 添加报警和通知功能

