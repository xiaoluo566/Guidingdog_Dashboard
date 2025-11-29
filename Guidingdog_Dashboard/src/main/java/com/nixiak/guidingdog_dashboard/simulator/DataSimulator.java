package com.nixiak.guidingdog_dashboard.simulator;

import com.nixiak.guidingdog_dashboard.entity.RadarData;
import com.nixiak.guidingdog_dashboard.entity.RobotDogData;
import com.nixiak.guidingdog_dashboard.service.RobotDogService;
import com.nixiak.guidingdog_dashboard.websocket.RealtimeDataWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 数据模拟器 - 用于测试
 * 模拟宇树机器狗发送实时数据
 */
@Slf4j
@Component
public class DataSimulator implements CommandLineRunner {

    @Autowired
    private RobotDogService robotDogService;

    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private double posX = 0.0;
    private double posY = 0.0;
    private double yaw = 0.0;

    @Override
    public void run(String... args) {
        log.info("启动数据模拟器...");

        // 每秒生成一次数据
        scheduler.scheduleAtFixedRate(this::generateAndSendData, 2, 1, TimeUnit.SECONDS);
    }

    private void generateAndSendData() {
        try {
            // 生成机器狗数据
            RobotDogData robotData = generateRobotDogData();
            robotDogService.saveRobotDogData(robotData);

            // 生成雷达数据
            RadarData radarData = generateRadarData();
            robotDogService.saveRadarData(radarData);

            // 通过WebSocket推送数据
            var realtimeData = robotDogService.getRealtimeData("DOG001");
            if (realtimeData != null) {
                RealtimeDataWebSocket.pushToClients("DOG001", realtimeData);
            }

            log.debug("模拟数据已发送");
        } catch (Exception e) {
            log.error("生成模拟数据失败", e);
        }
    }

    private RobotDogData generateRobotDogData() {
        RobotDogData data = new RobotDogData();

        // 基本信息
        data.setDogId("DOG001");
        data.setDogName("UnitreeGo1");

        // 模拟运动：随机游走
        double velocityX = (random.nextDouble() - 0.5) * 2; // -1 到 1 m/s
        double velocityY = (random.nextDouble() - 0.5) * 2;
        double velocityZ = (random.nextDouble() - 0.5) * 0.2;

        data.setVelocityX(round(velocityX));
        data.setVelocityY(round(velocityY));
        data.setVelocityZ(round(velocityZ));

        // 更新位置
        posX += velocityX * 0.1;
        posY += velocityY * 0.1;

        data.setPositionX(round(posX));
        data.setPositionY(round(posY));
        data.setPositionZ(round(0.0));

        // 加速度
        data.setAccelerationX(round((random.nextDouble() - 0.5) * 0.5));
        data.setAccelerationY(round((random.nextDouble() - 0.5) * 0.5));
        data.setAccelerationZ(round((random.nextDouble() - 0.5) * 0.2));

        // 姿态 - 方位角随机变化
        yaw += (random.nextDouble() - 0.5) * 10;
        if (yaw > 180) yaw -= 360;
        if (yaw < -180) yaw += 360;

        data.setYaw(round(yaw));
        data.setPitch(round((random.nextDouble() - 0.5) * 5));
        data.setRoll(round((random.nextDouble() - 0.5) * 5));

        // 状态
        String[] statuses = {"IDLE", "WALKING", "RUNNING", "STOPPED"};
        data.setStatus(statuses[random.nextInt(statuses.length)]);

        // 电池和温度
        data.setBatteryLevel(round(80 + random.nextDouble() * 20)); // 80-100%
        data.setTemperature(round(30 + random.nextDouble() * 10));  // 30-40°C

        data.setRecordTime(LocalDateTime.now());

        return data;
    }

    private RadarData generateRadarData() {
        RadarData data = new RadarData();

        data.setDogId("DOG001");
        data.setRadarId("RADAR01");
        data.setPointCount(1000 + random.nextInt(500));

        // 模拟障碍物距离
        double frontDist = 2.0 + random.nextDouble() * 5.0;
        double leftDist = 3.0 + random.nextDouble() * 5.0;
        double rightDist = 3.0 + random.nextDouble() * 5.0;
        double backDist = 4.0 + random.nextDouble() * 6.0;

        data.setFrontDistance(round(frontDist));
        data.setLeftDistance(round(leftDist));
        data.setRightDistance(round(rightDist));
        data.setBackDistance(round(backDist));
        data.setMinDistance(round(Math.min(Math.min(frontDist, leftDist),
                                           Math.min(rightDist, backDist))));

        // 可以在这里生成点云数据JSON
        data.setScanData("{}");

        data.setScanTime(LocalDateTime.now());

        return data;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

