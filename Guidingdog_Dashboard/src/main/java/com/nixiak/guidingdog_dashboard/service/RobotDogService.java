package com.nixiak.guidingdog_dashboard.service;

import com.nixiak.guidingdog_dashboard.dto.RealtimeDataDTO;
import com.nixiak.guidingdog_dashboard.entity.CameraData;
import com.nixiak.guidingdog_dashboard.entity.RadarData;
import com.nixiak.guidingdog_dashboard.entity.RobotDogData;
import com.nixiak.guidingdog_dashboard.mapper.CameraDataMapper;
import com.nixiak.guidingdog_dashboard.mapper.RadarDataMapper;
import com.nixiak.guidingdog_dashboard.mapper.RobotDogDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 机器狗数据服务
 */
@Slf4j
@Service
public class RobotDogService {

    @Autowired
    private RobotDogDataMapper robotDogDataMapper;

    @Autowired
    private CameraDataMapper cameraDataMapper;

    @Autowired
    private RadarDataMapper radarDataMapper;

    /**
     * 保存机器狗实时数据
     */
    public void saveRobotDogData(RobotDogData data) {
        data.setCreateTime(LocalDateTime.now());
        if (data.getRecordTime() == null) {
            data.setRecordTime(LocalDateTime.now());
        }
        robotDogDataMapper.insert(data);
        log.info("保存机器狗数据: dogId={}, status={}", data.getDogId(), data.getStatus());
    }

    /**
     * 保存摄像头数据
     */
    public void saveCameraData(CameraData data) {
        data.setCreateTime(LocalDateTime.now());
        if (data.getCaptureTime() == null) {
            data.setCaptureTime(LocalDateTime.now());
        }
        cameraDataMapper.insert(data);
        log.info("保存摄像头数据: dogId={}, cameraId={}", data.getDogId(), data.getCameraId());
    }

    /**
     * 保存雷达数据
     */
    public void saveRadarData(RadarData data) {
        data.setCreateTime(LocalDateTime.now());
        if (data.getScanTime() == null) {
            data.setScanTime(LocalDateTime.now());
        }
        radarDataMapper.insert(data);
        log.info("保存雷达数据: dogId={}, minDistance={}", data.getDogId(), data.getMinDistance());
    }

    /**
     * 获取机器狗实时数据
     */
    public RealtimeDataDTO getRealtimeData(String dogId) {
        RobotDogData robotData = robotDogDataMapper.getLatestByDogId(dogId);
        RadarData radarData = radarDataMapper.getLatestByDogId(dogId);

        if (robotData == null) {
            return null;
        }

        RealtimeDataDTO dto = new RealtimeDataDTO();

        // 基本信息
        dto.setDogId(robotData.getDogId());
        dto.setDogName(robotData.getDogName());
        dto.setStatus(robotData.getStatus());

        // 速度数据
        dto.setVelocityX(robotData.getVelocityX());
        dto.setVelocityY(robotData.getVelocityY());
        dto.setVelocityZ(robotData.getVelocityZ());
        // 计算合成速度
        if (robotData.getVelocityX() != null && robotData.getVelocityY() != null) {
            double speed = Math.sqrt(
                Math.pow(robotData.getVelocityX(), 2) +
                Math.pow(robotData.getVelocityY(), 2)
            );
            dto.setSpeed(Math.round(speed * 100.0) / 100.0);
        }

        // 加速度数据
        dto.setAccelerationX(robotData.getAccelerationX());
        dto.setAccelerationY(robotData.getAccelerationY());
        dto.setAccelerationZ(robotData.getAccelerationZ());

        // 姿态数据
        dto.setYaw(robotData.getYaw());
        dto.setPitch(robotData.getPitch());
        dto.setRoll(robotData.getRoll());

        // 位置数据
        dto.setPositionX(robotData.getPositionX());
        dto.setPositionY(robotData.getPositionY());
        dto.setPositionZ(robotData.getPositionZ());

        // 状态数据
        dto.setBatteryLevel(robotData.getBatteryLevel());
        dto.setTemperature(robotData.getTemperature());

        // 雷达数据
        if (radarData != null) {
            dto.setMinDistance(radarData.getMinDistance());
            dto.setFrontDistance(radarData.getFrontDistance());
            dto.setLeftDistance(radarData.getLeftDistance());
            dto.setRightDistance(radarData.getRightDistance());
            dto.setBackDistance(radarData.getBackDistance());
        }

        dto.setTimestamp(System.currentTimeMillis());

        return dto;
    }

    /**
     * 获取所有机器狗最新数据
     */
    public List<RobotDogData> getAllLatestData() {
        return robotDogDataMapper.getAllLatest();
    }

    /**
     * 获取摄像头图像
     */
    public CameraData getCameraImage(String dogId, String cameraId) {
        return cameraDataMapper.getLatest(dogId, cameraId);
    }

    /**
     * 获取所有摄像头图像
     */
    public List<CameraData> getAllCameraImages(String dogId) {
        return cameraDataMapper.getAllLatestByDogId(dogId);
    }

    /**
     * 获取雷达数据
     */
    public RadarData getRadarData(String dogId) {
        return radarDataMapper.getLatestByDogId(dogId);
    }

    /**
     * 获取历史数据
     */
    public List<RobotDogData> getHistoryData(String dogId, String startTime, String endTime, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 100;
        }
        return robotDogDataMapper.getHistoryData(dogId, startTime, endTime, limit);
    }
}

