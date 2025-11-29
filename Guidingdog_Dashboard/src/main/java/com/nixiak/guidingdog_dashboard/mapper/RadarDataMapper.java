package com.nixiak.guidingdog_dashboard.mapper;

import com.nixiak.guidingdog_dashboard.entity.RadarData;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 雷达数据Mapper
 */
@Mapper
public interface RadarDataMapper {

    /**
     * 插入雷达数据
     */
    @Insert("INSERT INTO radar_data (dog_id, radar_id, scan_data, point_count, " +
            "min_distance, front_distance, left_distance, right_distance, back_distance, " +
            "scan_time, create_time) " +
            "VALUES (#{dogId}, #{radarId}, #{scanData}, #{pointCount}, " +
            "#{minDistance}, #{frontDistance}, #{leftDistance}, #{rightDistance}, #{backDistance}, " +
            "#{scanTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(RadarData radarData);

    /**
     * 根据dogId获取最新雷达数据
     */
    @Select("SELECT * FROM radar_data WHERE dog_id = #{dogId} ORDER BY scan_time DESC LIMIT 1")
    RadarData getLatestByDogId(String dogId);

    /**
     * 根据dogId查询历史雷达数据
     */
    @Select("SELECT * FROM radar_data WHERE dog_id = #{dogId} " +
            "ORDER BY scan_time DESC LIMIT #{limit}")
    List<RadarData> getHistoryByDogId(@Param("dogId") String dogId, @Param("limit") Integer limit);

    /**
     * 删除指定时间之前的数据
     */
    @Delete("DELETE FROM radar_data WHERE scan_time < #{time}")
    int deleteOldData(String time);
}

