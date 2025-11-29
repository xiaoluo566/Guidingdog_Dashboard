# 测试数据示例

## 1. 测试上传机器狗数据

### 使用 curl 命令

```bash
curl -X POST http://localhost:8080/api/robot/data/upload \
  -H "Content-Type: application/json" \
  -d '{
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
    "temperature": 35.2
  }'
```

### 使用 PowerShell（Windows）

```powershell
$body = @{
    dogId = "DOG001"
    dogName = "UnitreeGo1"
    velocityX = 0.5
    velocityY = 0.2
    velocityZ = 0.0
    accelerationX = 0.1
    accelerationY = 0.05
    accelerationZ = 0.0
    yaw = 45.0
    pitch = 0.0
    roll = 0.0
    positionX = 10.5
    positionY = 20.3
    positionZ = 0.0
    status = "WALKING"
    batteryLevel = 85.5
    temperature = 35.2
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/robot/data/upload" -Method Post -Body $body -ContentType "application/json"
```

## 2. 测试上传雷达数据

```bash
curl -X POST http://localhost:8080/api/robot/radar/upload \
  -H "Content-Type: application/json" \
  -d '{
    "dogId": "DOG001",
    "radarId": "RADAR01",
    "pointCount": 1000,
    "minDistance": 2.5,
    "frontDistance": 3.0,
    "leftDistance": 4.5,
    "rightDistance": 5.0,
    "backDistance": 6.0
  }'
```

## 3. 测试获取实时数据

```bash
curl http://localhost:8080/api/robot/data/realtime/DOG001
```

## 4. 测试健康检查

```bash
curl http://localhost:8080/api/robot/health
```

## Python 测试脚本

```python
import requests
import json
import time
import random
import math

API_BASE = "http://localhost:8080/api/robot"

def send_robot_data():
    """发送机器狗数据"""
    data = {
        "dogId": "DOG001",
        "dogName": "UnitreeGo1",
        "velocityX": round(random.uniform(-1, 1), 2),
        "velocityY": round(random.uniform(-1, 1), 2),
        "velocityZ": round(random.uniform(-0.1, 0.1), 2),
        "accelerationX": round(random.uniform(-0.5, 0.5), 2),
        "accelerationY": round(random.uniform(-0.5, 0.5), 2),
        "accelerationZ": round(random.uniform(-0.2, 0.2), 2),
        "yaw": round(random.uniform(-180, 180), 2),
        "pitch": round(random.uniform(-10, 10), 2),
        "roll": round(random.uniform(-10, 10), 2),
        "positionX": round(random.uniform(0, 100), 2),
        "positionY": round(random.uniform(0, 100), 2),
        "positionZ": 0.0,
        "status": random.choice(["IDLE", "WALKING", "RUNNING", "STOPPED"]),
        "batteryLevel": round(random.uniform(80, 100), 2),
        "temperature": round(random.uniform(30, 40), 2)
    }
    
    response = requests.post(f"{API_BASE}/data/upload", json=data)
    print(f"上传机器狗数据: {response.json()}")

def send_radar_data():
    """发送雷达数据"""
    data = {
        "dogId": "DOG001",
        "radarId": "RADAR01",
        "pointCount": random.randint(800, 1200),
        "minDistance": round(random.uniform(1.5, 3.0), 2),
        "frontDistance": round(random.uniform(2.0, 5.0), 2),
        "leftDistance": round(random.uniform(3.0, 6.0), 2),
        "rightDistance": round(random.uniform(3.0, 6.0), 2),
        "backDistance": round(random.uniform(4.0, 8.0), 2)
    }
    
    response = requests.post(f"{API_BASE}/radar/upload", json=data)
    print(f"上传雷达数据: {response.json()}")

def get_realtime_data():
    """获取实时数据"""
    response = requests.get(f"{API_BASE}/data/realtime/DOG001")
    print(f"实时数据: {json.dumps(response.json(), indent=2, ensure_ascii=False)}")

if __name__ == "__main__":
    print("开始测试...")
    
    # 循环发送数据
    for i in range(10):
        print(f"\n--- 第 {i+1} 次发送 ---")
        send_robot_data()
        send_radar_data()
        time.sleep(1)
    
    # 获取实时数据
    print("\n--- 获取实时数据 ---")
    get_realtime_data()
```

## JavaScript 测试脚本（Node.js）

```javascript
const axios = require('axios');

const API_BASE = 'http://localhost:8080/api/robot';

async function sendRobotData() {
    const data = {
        dogId: 'DOG001',
        dogName: 'UnitreeGo1',
        velocityX: Math.random() * 2 - 1,
        velocityY: Math.random() * 2 - 1,
        velocityZ: Math.random() * 0.2 - 0.1,
        accelerationX: Math.random() - 0.5,
        accelerationY: Math.random() - 0.5,
        accelerationZ: Math.random() * 0.4 - 0.2,
        yaw: Math.random() * 360 - 180,
        pitch: Math.random() * 20 - 10,
        roll: Math.random() * 20 - 10,
        positionX: Math.random() * 100,
        positionY: Math.random() * 100,
        positionZ: 0,
        status: ['IDLE', 'WALKING', 'RUNNING', 'STOPPED'][Math.floor(Math.random() * 4)],
        batteryLevel: 80 + Math.random() * 20,
        temperature: 30 + Math.random() * 10
    };
    
    try {
        const response = await axios.post(`${API_BASE}/data/upload`, data);
        console.log('上传成功:', response.data);
    } catch (error) {
        console.error('上传失败:', error.message);
    }
}

async function getRealtimeData() {
    try {
        const response = await axios.get(`${API_BASE}/data/realtime/DOG001`);
        console.log('实时数据:', JSON.stringify(response.data, null, 2));
    } catch (error) {
        console.error('获取失败:', error.message);
    }
}

// 测试
(async () => {
    for (let i = 0; i < 5; i++) {
        console.log(`\n--- 第 ${i+1} 次发送 ---`);
        await sendRobotData();
        await new Promise(resolve => setTimeout(resolve, 1000));
    }
    
    console.log('\n--- 获取实时数据 ---');
    await getRealtimeData();
})();
```

## WebSocket 测试（浏览器控制台）

```javascript
// 在浏览器控制台中执行
const ws = new WebSocket('ws://localhost:8080/ws/realtime/DOG001');

ws.onopen = () => {
    console.log('WebSocket 连接成功');
};

ws.onmessage = (event) => {
    const data = JSON.parse(event.data);
    console.log('收到数据:', data);
};

ws.onerror = (error) => {
    console.error('WebSocket 错误:', error);
};

ws.onclose = () => {
    console.log('WebSocket 连接关闭');
};
```

