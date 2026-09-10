package com.vsu.iscr.controller;

import com.vsu.iscr.core.vo.ResultVo;
import com.vsu.iscr.domain.Reservation;
import com.vsu.iscr.service.ReservationService;
import com.vsu.iscr.utils.ResultVoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约控制器
 * 
 * @author iscr
 */
@Slf4j
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * 获取所有预约列表
     */
    @GetMapping("/all")
    public ResultVo getAllReservations() {
        try {
            List<Reservation> list = reservationService.selectAllReservations();
            return ResultVoUtil.success(list);
        } catch (Exception e) {
            log.error("获取所有预约失败", e);
            return ResultVoUtil.error("获取所有预约失败：" + e.getMessage());
        }
    }

    /**
     * 根据条件获取预约列表
     */
    @GetMapping("/list")
    public ResultVo getReservationList(Reservation reservation) {
        try {
            List<Reservation> list = reservationService.selectReservationList(reservation);
            return ResultVoUtil.success(list);
        } catch (Exception e) {
            log.error("获取预约列表失败", e);
            return ResultVoUtil.error("获取预约列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取预约详情
     */
    @GetMapping("/{id}")
    public ResultVo getReservationById(@PathVariable("id") Integer id) {
        try {
            Reservation reservation = reservationService.selectReservationById(id);
            if (reservation == null) {
                return ResultVoUtil.error("预约不存在");
            }
            return ResultVoUtil.success(reservation);
        } catch (Exception e) {
            log.error("获取预约详情失败", e);
            return ResultVoUtil.error("获取预约详情失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID获取预约列表
     */
    @GetMapping("/user/{userId}")
    public ResultVo getReservationsByUserId(@PathVariable("userId") Integer userId) {
        try {
            List<Reservation> list = reservationService.selectReservationsByUserId(userId);
            return ResultVoUtil.success(list);
        } catch (Exception e) {
            log.error("获取用户预约列表失败", e);
            return ResultVoUtil.error("获取用户预约列表失败：" + e.getMessage());
        }
    }

    /**
     * 创建预约
     */
    @PostMapping
    public ResultVo createReservation(@RequestBody Reservation reservation) {
        try {
            int result = reservationService.insertReservation(reservation);
            if (result > 0) {
                return ResultVoUtil.success(reservation);
            }
            return ResultVoUtil.error("预约失败");
        } catch (RuntimeException e) {
            log.error("创建预约失败", e);
            return ResultVoUtil.error(e.getMessage());
        } catch (Exception e) {
            log.error("创建预约失败", e);
            return ResultVoUtil.error("创建预约失败：" + e.getMessage());
        }
    }

    /**
     * 更新预约信息
     */
    @PutMapping("/{id}")
    public ResultVo updateReservation(@PathVariable("id") Integer id, @RequestBody Reservation reservation) {
        try {
            reservation.setReservationId(id);
            int result = reservationService.updateReservation(reservation);
            if (result > 0) {
                return ResultVoUtil.success("更新成功");
            }
            return ResultVoUtil.error("更新失败");
        } catch (Exception e) {
            log.error("更新预约失败", e);
            return ResultVoUtil.error("更新预约失败：" + e.getMessage());
        }
    }

    /**
     * 更新预约状态
     */
    @PatchMapping("/{id}/status")
    public ResultVo updateReservationStatus(@PathVariable("id") Integer id, @RequestParam("status") Integer status) {
        try {
            int result = reservationService.updateReservationStatus(id, status);
            if (result > 0) {
                return ResultVoUtil.success("状态更新成功");
            }
            return ResultVoUtil.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新预约状态失败", e);
            return ResultVoUtil.error("更新预约状态失败：" + e.getMessage());
        }
    }

    /**
     * 取消预约
     */
    @DeleteMapping("/{id}")
    public ResultVo cancelReservation(@PathVariable("id") Integer id) {
        try {
            int result = reservationService.deleteReservationById(id);
            if (result > 0) {
                return ResultVoUtil.success("取消预约成功");
            }
            return ResultVoUtil.error("取消预约失败");
        } catch (Exception e) {
            log.error("取消预约失败", e);
            return ResultVoUtil.error("取消预约失败：" + e.getMessage());
        }
    }
}
