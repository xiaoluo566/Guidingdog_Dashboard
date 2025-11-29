package com.nixiak.guidingdog_dashboard.mapper;

import com.nixiak.guidingdog_dashboard.entity.CameraData;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 摄像头数据Mapper
 */
@Mapper
public interface CameraDataMapper {

    /**
     * 插入摄像头数据
     */
    @Insert("INSERT INTO camera_data (dog_id, camera_id, image_url, width, height, format, " +
            "capture_time, create_time) " +
            "VALUES (#{dogId}, #{cameraId}, #{imageUrl}, #{width}, #{height}, #{format}, " +
            "#{captureTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CameraData cameraData);

    /**
     * 根据dogId和cameraId获取最新图像
     */
    @Select("SELECT * FROM camera_data WHERE dog_id = #{dogId} AND camera_id = #{cameraId} " +
            "ORDER BY capture_time DESC LIMIT 1")
    CameraData getLatest(@Param("dogId") String dogId, @Param("cameraId") String cameraId);

    /**
     * 根据dogId获取所有摄像头最新图像
     */
    @Select("SELECT * FROM camera_data WHERE id IN " +
            "(SELECT MAX(id) FROM camera_data WHERE dog_id = #{dogId} GROUP BY camera_id)")
    List<CameraData> getAllLatestByDogId(String dogId);

    /**
     * 删除指定时间之前的数据
     */
    @Delete("DELETE FROM camera_data WHERE capture_time < #{time}")
    int deleteOldData(String time);
}

