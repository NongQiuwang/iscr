package com.vsu.iscr.service;

import com.vsu.iscr.domain.Car;
import com.vsu.iscr.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 汽车业务接口
 *
 */

public interface CarService {



    /**
     * 查询所有汽车
     */
     List<Car> selectAllCars();

    /**
     * 根据ID查询汽车
     */
     Car selectCarById(Integer carId);

    /**
     * 根据条件查询汽车列表
     */
     List<Car> selectCarList(Car car);

    /**
     * 新增汽车
     */
     int insertCar(Car car) ;


    /**
     * 更新汽车信息
     */
     int updateCar(Car car);

    /**
     * 删除汽车
     */
     int deleteCarById(Integer carId);

    /**
     * 批量删除汽车
     */
     int deleteCarByIds(Integer[] carIds);

    /**
     * 更新汽车状态
     */
     int updateCarStatus(Integer carId, Integer status) ;
}
