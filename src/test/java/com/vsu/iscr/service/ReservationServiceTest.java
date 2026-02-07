package com.vsu.iscr.service;

import com.vsu.iscr.domain.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 预约服务测试类
 * 用于测试 ReservationService 的各项功能
 */
@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService resService;

    /**
     * 测试创建预约成功
     * 验证创建一个有效的预约后返回值大于0
     */
    @Test
    void testCreateReservationSuccess() {
        Reservation res = new Reservation();
        res.setUserId(1);
        res.setCarId(2);
        res.setStartDate(LocalDate.now());
        res.setEndDate(LocalDate.now().plusDays(3));
        int result = resService.insertReservation(res);
        assertTrue(result > 0);
    }

    /**
     * 测试查询所有预约列表
     * 验证预约列表查询功能正常
     */
    @Test
    void testSelectAllReservations() {
        List<Reservation> reservations = resService.selectAllReservations();
        assertNotNull(reservations);
    }

    /**
     * 测试根据用户ID查询预约
     * 验证用户预约查询功能正常
     */
    @Test
    void testSelectReservationsByUserId() {
        List<Reservation> reservations = resService.selectReservationsByUserId(1);
        assertNotNull(reservations);
    }

    /**
     * 测试根据汽车ID查询预约
     * 验证汽车预约查询功能正常
     */
    @Test
    void testSelectReservationsByCarId() {
        List<Reservation> reservations = resService.selectReservationsByCarId(2);
        assertNotNull(reservations);
    }

    /**
     * 测试更新预约状态
     * 验证将预约状态更新为已确认（状态码1）是否成功
     */
    @Test
    void testUpdateReservationStatus() {
        // Create a reservation first
        Reservation res = new Reservation();
        res.setUserId(1);
        res.setCarId(3);
        res.setStartDate(LocalDate.now().plusDays(10));
        res.setEndDate(LocalDate.now().plusDays(15));
        resService.insertReservation(res);

        List<Reservation> reservations = resService.selectReservationsByUserId(1);
        if (!reservations.isEmpty()) {
            Integer reservationId = reservations.get(reservations.size() - 1).getReservationId();

            // Update status to confirmed
            int result = resService.updateReservationStatus(reservationId, 1);
            assertTrue(result > 0);
        }
    }

    /**
     * 测试创建预约时日期冲突的情况
     * 验证当预约日期与已有预约冲突时，系统的处理
     */
    @Test
    void testCreateReservationWithDateConflict() {

        Reservation res1 = new Reservation();
        res1.setUserId(1);
        res1.setCarId(4);
        res1.setStartDate(LocalDate.now().plusDays(20));
        res1.setEndDate(LocalDate.now().plusDays(25));
        resService.insertReservation(res1);

        Reservation res2 = new Reservation();
        res2.setUserId(2);
        res2.setCarId(4);
        res2.setStartDate(LocalDate.now().plusDays(22));
        res2.setEndDate(LocalDate.now().plusDays(28));

        try {
            int result = resService.insertReservation(res2);
        } catch (RuntimeException e) {
            assertNotNull(e.getMessage());
        }
    }

    /**
     * 测试取消预约
     * 验证将预约状态更新为已取消（状态码2）是否成功
     */
    @Test
    void testCancelReservation() {
        Reservation res = new Reservation();
        res.setUserId(1);
        res.setCarId(5);
        res.setStartDate(LocalDate.now().plusDays(30));
        res.setEndDate(LocalDate.now().plusDays(35));
        resService.insertReservation(res);

        List<Reservation> reservations = resService.selectReservationsByUserId(1);
        if (!reservations.isEmpty()) {
            Integer reservationId = reservations.get(reservations.size() - 1).getReservationId();

            int result = resService.updateReservationStatus(reservationId, 2);
            assertTrue(result > 0);
        }
    }
}
