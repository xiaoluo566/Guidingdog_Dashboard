package com.nixiak.guidingdog_dashboard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 摄像头数据实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraData {

    private Long id;

    private String dogId;           // 机器狗ID
    private String cameraId;        // 摄像头ID (front, back, left, right)
    private String imageUrl;        // 图像URL或Base64数据
    private Integer width;          // 图像宽度
    private Integer height;         // 图像高度
    private String format;          // 图像格式 (jpg, png)

    private LocalDateTime captureTime;  // 拍摄时间
    private LocalDateTime createTime;   // 创建时间
}

