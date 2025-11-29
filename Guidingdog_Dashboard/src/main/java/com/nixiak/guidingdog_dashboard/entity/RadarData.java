package com.nixiak.guidingdog_dashboard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 雷达数据实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RadarData {

    private Long id;

    private String dogId;           // 机器狗ID
    private String radarId;         // 雷达ID

    // 雷达扫描数据（存储为JSON字符串）
    private String scanData;        // 雷达扫描点云数据
    private Integer pointCount;     // 点云数量

    // 障碍物检测
    private Double minDistance;     // 最近障碍物距离 (m)
    private Double frontDistance;   // 前方距离 (m)
    private Double leftDistance;    // 左侧距离 (m)
    private Double rightDistance;   // 右侧距离 (m)
    private Double backDistance;    // 后方距离 (m)

    private LocalDateTime scanTime;     // 扫描时间
    private LocalDateTime createTime;   // 创建时间
}

