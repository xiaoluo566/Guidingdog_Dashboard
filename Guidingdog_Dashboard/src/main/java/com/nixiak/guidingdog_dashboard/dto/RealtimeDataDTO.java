package com.nixiak.guidingdog_dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实时数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeDataDTO {

    // 机器狗基本信息
    private String dogId;
    private String dogName;
    private String status;

    // 速度数据
    private Double velocityX;
    private Double velocityY;
    private Double velocityZ;
    private Double speed;           // 合成速度

    // 加速度数据
    private Double accelerationX;
    private Double accelerationY;
    private Double accelerationZ;

    // 姿态数据
    private Double yaw;             // 方位角
    private Double pitch;
    private Double roll;

    // 位置数据
    private Double positionX;
    private Double positionY;
    private Double positionZ;

    // 状态数据
    private Double batteryLevel;
    private Double temperature;

    // 雷达数据
    private Double minDistance;
    private Double frontDistance;
    private Double leftDistance;
    private Double rightDistance;
    private Double backDistance;

    // 时间戳
    private Long timestamp;
}

