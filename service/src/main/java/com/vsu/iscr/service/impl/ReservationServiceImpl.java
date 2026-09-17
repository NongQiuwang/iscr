package com.vsu.iscr.service.impl;

import com.vsu.iscr.domain.Car;
import com.vsu.iscr.domain.Reservation;
import com.vsu.iscr.mapper.ReservationMapper;
import com.vsu.iscr.service.CarService;
import com.vsu.iscr.service.ReservationService;
import com.vsu.iscr.service.UsersService;
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

    @Autowired
    private UsersService usersService;

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
     * 新增预约（带车辆可用性检查与边界校验）
     */
    public int insertReservation(Reservation reservation) {
        if (reservation.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (reservation.getCarId() == null) {
            throw new RuntimeException("车辆ID不能为空");
        }
        if (reservation.getStartDate() == null || reservation.getEndDate() == null) {
            throw new RuntimeException("预约日期不能为空");
        }
        if (reservation.getEndDate().isBefore(reservation.getStartDate())) {
            throw new RuntimeException("结束日期不能早于开始日期");
        }
        if (usersService.selectUserById(reservation.getUserId()) == null) {
            throw new RuntimeException("用户不存在");
        }

        Car car = carService.selectCarById(reservation.getCarId());
        if (car == null) {
            throw new RuntimeException("车辆不存在");
        }
        if (car.getStatus() != null && car.getStatus() != 0) {
            throw new RuntimeException("该车辆当前不可租");
        }

        int count = reservationMapper.checkCarAvailability(
                reservation.getCarId(),
                reservation.getStartDate().toString(),
                reservation.getEndDate().toString());

        if (count > 0) {
            throw new RuntimeException("该车辆在选定日期内已被预约");
        }

        long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()) + 1;
        BigDecimal dailyRent = car.getDailyRent();
        reservation.setTotalPrice(dailyRent.multiply(new BigDecimal(days)));

        if (reservation.getStatus() == null) {
            reservation.setStatus(0);
        }

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
        Reservation reservation = reservationMapper.selectReservationById(reservationId);
        if (reservation != null) {
            carService.updateCarStatus(reservation.getCarId(), 0);
        }
        return reservationMapper.deleteReservationById(reservationId);
    }

    /**
     * 更新预约状态
     */
    public int updateReservationStatus(Integer reservationId, Integer status) {
        if (status == 2 || status == 3) {
            Reservation reservation = reservationMapper.selectReservationById(reservationId);
            if (reservation != null) {
                carService.updateCarStatus(reservation.getCarId(), 0);
            }
        }
        return reservationMapper.updateReservationStatus(reservationId, status);
    }

    /**
     * 取消预约（软取消：状态改为已取消并恢复车辆可租）
     */
    public int cancelReservation(Integer reservationId) {
        Reservation reservation = reservationMapper.selectReservationById(reservationId);
        if (reservation == null) {
            throw new RuntimeException("预约不存在");
        }
        if (reservation.getStatus() != null && reservation.getStatus() == 2) {
            return 1;
        }
        carService.updateCarStatus(reservation.getCarId(), 0);
        return reservationMapper.updateReservationStatus(reservationId, 2);
    }
}
