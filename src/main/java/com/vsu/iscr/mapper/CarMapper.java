package com.vsu.iscr.mapper;

import com.vsu.iscr.domain.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 汽车数据访问接口
 *
 */
@Mapper
public interface CarMapper {

    /**
     * 查询所有汽车
     */
    List<Car> selectAllCars();

    /**
     * 根据ID查询汽车
     */
    Car selectCarById(@Param("carId") Integer carId);

    /**
     * 根据条件查询汽车列表
     */
    List<Car> selectCarList(Car car);

    /**
     * 新增汽车
     */
    int insertCar(Car car);

    /**
     * 更新汽车信息
     */
    int updateCar(Car car);

    /**
     * 删除汽车
     */
    int deleteCarById(@Param("carId") Integer carId);

    /**
     * 批量删除汽车
     */
    int deleteCarByIds(@Param("carIds") Integer[] carIds);

    /**
     * 更新汽车状态
     */
    int updateCarStatus(@Param("carId") Integer carId, @Param("status") Integer status);
}
