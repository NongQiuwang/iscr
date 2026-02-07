package com.vsu.iscr.service;

import com.vsu.iscr.domain.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 汽车服务测试类
 *
 */
@SpringBootTest
class CarServiceTest {

    @Autowired
    private CarService carService;

    /**
     * 测试根据ID查询汽车
     * 验证查询ID为1的汽车是否存在且ID正确
     */
    @Test
    void testSelectCarById() {
        Car car = carService.selectCarById(1);
        assertNotNull(car);
        assertEquals(1, car.getCarId());
    }

    /**
     * 测试插入重复车牌的汽车
     * 验证当尝试插入已存在车牌的汽车时，系统应抛出异常
     */
    @Test
    void testInsertCarWithDuplicatePlate() {
        Car car = new Car();
        car.setBrand("Audi");
        car.setSeries("A6L");
        car.setYear("2024");
        car.setModel("45 TFSI quattro Luxury Sport");
        assertThrows(RuntimeException.class, () -> carService.insertCar(car));
    }

    /**
     * 测试查询所有汽车列表
     * 验证汽车列表不为空且数量大于0
     */
    @Test
    void testSelectAllCars() {
        List<Car> cars = carService.selectAllCars();
        assertNotNull(cars);
        assertTrue(cars.size() > 0);
    }

    /**
     * 测试新增汽车功能
     * 验证插入一辆新车后返回值大于0
     */
    @Test
    void testInsertCar() {
        Car car = new Car();
        car.setBrand("Honda");
        car.setSeries("Accord");
        car.setYear("2024");
        car.setModel("260 TURBO Luxury Edition");
        car.setCategory("Mid-size Sedan");
        car.setDisplacement("1.5T");
        car.setSeatCount(5);
        car.setTransmission("automatic");
        car.setDoorCount(4);
        car.setFuelType("Gasoline");
        car.setFuelLabel("92 Octane");
        car.setStatus(0);

        int result = carService.insertCar(car);
        assertTrue(result > 0);
    }

    /**
     * 测试更新汽车状态
     * 验证将汽车状态更新为维护中（状态码2）是否成功
     */
    @Test
    void testUpdateCarStatus() {
        int result = carService.updateCarStatus(1, 2);
        assertTrue(result > 0);

        Car car = carService.selectCarById(1);
        assertEquals(2, car.getStatus());
        carService.updateCarStatus(1, 0);
    }
}
