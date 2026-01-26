package com.vsu.iscr.service.impl;

import com.vsu.iscr.domain.Reservation;
import com.vsu.iscr.mapper.ReservationMapper;
import com.vsu.iscr.service.CarService;
import com.vsu.iscr.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private CarService carService;

    /**
     * 查询所有预约
     */
    public List<Reservation> selectAllReservations() {
        return reservationMapper.selectAllReservations();
    }

    /**
     * 根据ID查询预约
     */
    public Reservation selectReservationById(Integer reservationId) {
        return reservationMapper.selectReservationById(reservationId);
    }

    /**
     * 根据用户ID查询预约列表
     */
    public List<Reservation> selectReservationsByUserId(Integer userId) {
        return reservationMapper.selectReservationsByUserId(userId);
    }

    /**
     * 根据汽车ID查询预约列表
     */
    public List<Reservation> selectReservationsByCarId(Integer carId) {
        return reservationMapper.selectReservationsByCarId(carId);
    }

    /**
     * 根据条件查询预约列表
     */
    public List<Reservation> selectReservationList(Reservation reservation) {
        return reservationMapper.selectReservationList(reservation);
    }

    /**
     * 新增预约（带车辆可用性检查）
     */

    public int insertReservation(Reservation reservation) {
        // 检查汽车在指定日期范围内是否可用
        int count = reservationMapper.checkCarAvailability(
                reservation.getCarId(),
                reservation.getStartDate().toString(),
                reservation.getEndDate().toString());

        if (count > 0) {
            throw new RuntimeException("该车辆在选定日期内已被预约");
        }

        // 计算总价
        long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()) + 1;
        BigDecimal dailyRent = carService.selectCarById(reservation.getCarId()).getDailyRent();
        reservation.setTotalPrice(dailyRent.multiply(new BigDecimal(days)));

        // 默认状态为待确认
        if (reservation.getStatus() == null) {
            reservation.setStatus(0);
        }

        // 更新汽车状态为已租
        carService.updateCarStatus(reservation.getCarId(), 1);

        return reservationMapper.insertReservation(reservation);
    }

    /**
     * 更新预约信息
     */
    public int updateReservation(Reservation reservation) {
        return reservationMapper.updateReservation(reservation);
    }

    /**
     * 删除预约
     */

    public int deleteReservationById(Integer reservationId) {
        // 获取预约信息
        Reservation reservation = reservationMapper.selectReservationById(reservationId);
        if (reservation != null) {
            // 恢复汽车状态为可租
            carService.updateCarStatus(reservation.getCarId(), 0);
        }
        return reservationMapper.deleteReservationById(reservationId);
    }

    /**
     * 更新预约状态
     */

    public int updateReservationStatus(Integer reservationId, Integer status) {
        // 如果状态为已取消或已完成，恢复汽车状态为可租
        if (status == 2 || status == 3) {
            Reservation reservation = reservationMapper.selectReservationById(reservationId);
            if (reservation != null) {
                carService.updateCarStatus(reservation.getCarId(), 0);
            }
        }
        return reservationMapper.updateReservationStatus(reservationId, status);
    }

}
