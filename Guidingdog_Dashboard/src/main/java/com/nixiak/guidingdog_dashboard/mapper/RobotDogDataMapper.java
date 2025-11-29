package com.nixiak.guidingdog_dashboard.mapper;

import com.nixiak.guidingdog_dashboard.entity.RobotDogData;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 机器狗数据Mapper
 */
@Mapper
public interface RobotDogDataMapper {

    /**
     * 插入机器狗数据
     */
    @Insert("INSERT INTO robot_dog_data (dog_id, dog_name, velocity_x, velocity_y, velocity_z, " +
            "acceleration_x, acceleration_y, acceleration_z, yaw, pitch, roll, " +
            "position_x, position_y, position_z, status, battery_level, temperature, " +
            "record_time, create_time) " +
            "VALUES (#{dogId}, #{dogName}, #{velocityX}, #{velocityY}, #{velocityZ}, " +
            "#{accelerationX}, #{accelerationY}, #{accelerationZ}, #{yaw}, #{pitch}, #{roll}, " +
            "#{positionX}, #{positionY}, #{positionZ}, #{status}, #{batteryLevel}, #{temperature}, " +
            "#{recordTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(RobotDogData robotDogData);

    /**
     * 根据dogId获取最新数据
     */
    @Select("SELECT * FROM robot_dog_data WHERE dog_id = #{dogId} ORDER BY record_time DESC LIMIT 1")
    RobotDogData getLatestByDogId(String dogId);

    /**
     * 获取所有机器狗的最新数据
     */
    @Select("SELECT * FROM robot_dog_data WHERE id IN " +
            "(SELECT MAX(id) FROM robot_dog_data GROUP BY dog_id)")
    List<RobotDogData> getAllLatest();

    /**
     * 根据dogId和时间范围查询历史数据
     */
    @Select("SELECT * FROM robot_dog_data WHERE dog_id = #{dogId} " +
            "AND record_time BETWEEN #{startTime} AND #{endTime} " +
            "ORDER BY record_time DESC LIMIT #{limit}")
    List<RobotDogData> getHistoryData(@Param("dogId") String dogId,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime,
                                       @Param("limit") Integer limit);

    /**
     * 删除指定时间之前的数据
     */
    @Delete("DELETE FROM robot_dog_data WHERE record_time < #{time}")
    int deleteOldData(String time);
}

