-- 导盲犬仪表盘数据库初始化脚本

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

-- 创建定时清理旧数据的事件（可选，保留最近30天的数据）
-- DELIMITER $$
-- CREATE EVENT IF NOT EXISTS clean_old_data
-- ON SCHEDULE EVERY 1 DAY
-- DO
-- BEGIN
--     DELETE FROM robot_dog_data WHERE record_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
--     DELETE FROM camera_data WHERE capture_time < DATE_SUB(NOW(), INTERVAL 7 DAY);
--     DELETE FROM radar_data WHERE scan_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
-- END$$
-- DELIMITER ;

