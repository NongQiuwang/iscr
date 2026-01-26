package com.vsu.iscr.service;

import com.vsu.iscr.domain.Reservation;
import com.vsu.iscr.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 预约业务接口
 *
 */

public interface ReservationService {



    /**
     * 查询所有预约
     */
     List<Reservation> selectAllReservations();

    /**
     * 根据ID查询预约
     */
     Reservation selectReservationById(Integer reservationId) ;

    /**
     * 根据用户ID查询预约列表
     */
     List<Reservation> selectReservationsByUserId(Integer userId);


    /**
     * 根据汽车ID查询预约列表
     */
     List<Reservation> selectReservationsByCarId(Integer carId);

    /**
     * 根据条件查询预约列表
     */
     List<Reservation> selectReservationList(Reservation reservation) ;

    /**
     * 新增预约（带车辆可用性检查）
     */

     int insertReservation(Reservation reservation);

    /**
     * 更新预约信息
     */
     int updateReservation(Reservation reservation);

    /**
     * 删除预约
     */
    @Transactional
    public int deleteReservationById(Integer reservationId) ;

    /**
     * 更新预约状态
     */

     int updateReservationStatus(Integer reservationId, Integer status) ;
}
