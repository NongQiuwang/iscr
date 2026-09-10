package com.vsu.iscr.mapper;

import com.vsu.iscr.domain.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预约数据访问接口
 *
 */
@Mapper
public interface ReservationMapper {

    /**
     * 查询所有预约
     */
    List<Reservation> selectAllReservations();

    /**
     * 根据ID查询预约
     */
    Reservation selectReservationById(@Param("reservationId") Integer reservationId);

    /**
     * 根据用户ID查询预约列表
     */
    List<Reservation> selectReservationsByUserId(@Param("userId") Integer userId);

    /**
     * 根据汽车ID查询预约列表
     */
    List<Reservation> selectReservationsByCarId(@Param("carId") Integer carId);

    /**
     * 根据条件查询预约列表
     */
    List<Reservation> selectReservationList(Reservation reservation);

    /**
     * 新增预约
     */
    int insertReservation(Reservation reservation);

    /**
     * 更新预约信息
     */
    int updateReservation(Reservation reservation);

    /**
     * 删除预约
     */
    int deleteReservationById(@Param("reservationId") Integer reservationId);

    /**
     * 更新预约状态
     */
    int updateReservationStatus(@Param("reservationId") Integer reservationId, @Param("status") Integer status);

    /**
     * 检查汽车在指定日期范围内是否已被预约
     */
    int checkCarAvailability(@Param("carId") Integer carId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);
}
