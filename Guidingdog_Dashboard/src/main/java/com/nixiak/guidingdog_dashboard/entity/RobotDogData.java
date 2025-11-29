package com.nixiak.guidingdog_dashboard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 机器狗实时数据实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobotDogData {

    private Long id;

    // 基本信息
    private String dogId;           // 机器狗ID
    private String dogName;         // 机器狗名称

    // 运动数据
    private Double velocityX;       // X轴速度 (m/s)
    private Double velocityY;       // Y轴速度 (m/s)
    private Double velocityZ;       // Z轴速度 (m/s)

    // 加速度数据
    private Double accelerationX;   // X轴加速度 (m/s²)
    private Double accelerationY;   // Y轴加速度 (m/s²)
    private Double accelerationZ;   // Z轴加速度 (m/s²)

    // 姿态数据
    private Double yaw;             // 偏航角/方位角 (度)
    private Double pitch;           // 俯仰角 (度)
    private Double roll;            // 横滚角 (度)

    // 位置数据
    private Double positionX;       // X坐标
    private Double positionY;       // Y坐标
    private Double positionZ;       // Z坐标

    // 状态数据
    private String status;          // 运行状态: IDLE, WALKING, RUNNING, STOPPED
    private Double batteryLevel;    // 电池电量 (%)
    private Double temperature;     // 温度 (℃)

    // 时间戳
    private LocalDateTime recordTime;   // 记录时间
    private LocalDateTime createTime;   // 创建时间
}

