package com.nixiak.guidingdog_dashboard.controller;

import com.nixiak.guidingdog_dashboard.dto.RealtimeDataDTO;
import com.nixiak.guidingdog_dashboard.dto.Result;
import com.nixiak.guidingdog_dashboard.entity.CameraData;
import com.nixiak.guidingdog_dashboard.entity.RadarData;
import com.nixiak.guidingdog_dashboard.entity.RobotDogData;
import com.nixiak.guidingdog_dashboard.service.RobotDogService;
import com.nixiak.guidingdog_dashboard.websocket.RealtimeDataWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 机器狗数据控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/robot")
public class RobotDogController {

    @Autowired
    private RobotDogService robotDogService;

    /**
     * 上传机器狗实时数据（由宇树机器狗或数据采集端调用）
     */
    @PostMapping("/data/upload")
    public Result<Void> uploadData(@RequestBody RobotDogData data) {
        try {
            robotDogService.saveRobotDogData(data);

            // 通过WebSocket推送给前端
            RealtimeDataDTO realtimeData = robotDogService.getRealtimeData(data.getDogId());
            if (realtimeData != null) {
                RealtimeDataWebSocket.pushToClients(data.getDogId(), realtimeData);
            }

            return Result.success();
        } catch (Exception e) {
            log.error("上传数据失败", e);
            return Result.error("上传数据失败: " + e.getMessage());
        }
    }

    /**
     * 上传摄像头图像
     */
    @PostMapping("/camera/upload")
    public Result<Void> uploadCameraImage(@RequestBody CameraData data) {
        try {
            robotDogService.saveCameraData(data);

            // 推送图像更新通知
            RealtimeDataWebSocket.pushToClients(data.getDogId(), data);

            return Result.success();
        } catch (Exception e) {
            log.error("上传摄像头图像失败", e);
            return Result.error("上传图像失败: " + e.getMessage());
        }
    }

    /**
     * 上传雷达数据
     */
    @PostMapping("/radar/upload")
    public Result<Void> uploadRadarData(@RequestBody RadarData data) {
        try {
            robotDogService.saveRadarData(data);

            // 推送雷达数据
            RealtimeDataWebSocket.pushToClients(data.getDogId(), data);

            return Result.success();
        } catch (Exception e) {
            log.error("上传雷达数据失败", e);
            return Result.error("上传雷达数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取机器狗实时数据
     */
    @GetMapping("/data/realtime/{dogId}")
    public Result<RealtimeDataDTO> getRealtimeData(@PathVariable String dogId) {
        try {
            RealtimeDataDTO data = robotDogService.getRealtimeData(dogId);
            if (data == null) {
                return Result.error(404, "未找到机器狗数据");
            }
            return Result.success(data);
        } catch (Exception e) {
            log.error("获取实时数据失败", e);
            return Result.error("获取数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有机器狗最新数据
     */
    @GetMapping("/data/all")
    public Result<List<RobotDogData>> getAllLatestData() {
        try {
            List<RobotDogData> dataList = robotDogService.getAllLatestData();
            return Result.success(dataList);
        } catch (Exception e) {
            log.error("获取所有数据失败", e);
            return Result.error("获取数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取摄像头图像
     */
    @GetMapping("/camera/{dogId}/{cameraId}")
    public Result<CameraData> getCameraImage(@PathVariable String dogId,
                                              @PathVariable String cameraId) {
        try {
            CameraData data = robotDogService.getCameraImage(dogId, cameraId);
            if (data == null) {
                return Result.error(404, "未找到摄像头图像");
            }
            return Result.success(data);
        } catch (Exception e) {
            log.error("获取摄像头图像失败", e);
            return Result.error("获取图像失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有摄像头图像
     */
    @GetMapping("/camera/all/{dogId}")
    public Result<List<CameraData>> getAllCameraImages(@PathVariable String dogId) {
        try {
            List<CameraData> dataList = robotDogService.getAllCameraImages(dogId);
            return Result.success(dataList);
        } catch (Exception e) {
            log.error("获取所有摄像头图像失败", e);
            return Result.error("获取图像失败: " + e.getMessage());
        }
    }

    /**
     * 获取雷达数据
     */
    @GetMapping("/radar/{dogId}")
    public Result<RadarData> getRadarData(@PathVariable String dogId) {
        try {
            RadarData data = robotDogService.getRadarData(dogId);
            if (data == null) {
                return Result.error(404, "未找到雷达数据");
            }
            return Result.success(data);
        } catch (Exception e) {
            log.error("获取雷达数据失败", e);
            return Result.error("获取数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取历史数据
     */
    @GetMapping("/data/history/{dogId}")
    public Result<List<RobotDogData>> getHistoryData(
            @PathVariable String dogId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<RobotDogData> dataList = robotDogService.getHistoryData(dogId, startTime, endTime, limit);
            return Result.success(dataList);
        } catch (Exception e) {
            log.error("获取历史数据失败", e);
            return Result.error("获取数据失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("服务运行正常");
    }
}

