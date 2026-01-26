package com.vsu.iscr.service.impl;

import com.vsu.iscr.domain.Car;
import com.vsu.iscr.mapper.CarMapper;
import com.vsu.iscr.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



@Service
@Transactional
public class CarServiceImpl implements CarService {
    @Autowired
    private CarMapper carMapper;

    @Override
    public List<Car> selectAllCars() {
        return carMapper.selectAllCars();
    }

    @Override
    public Car selectCarById(Integer carId) {
        return carMapper.selectCarById(carId);
    }

    @Override
    public List<Car> selectCarList(Car car) {
        return carMapper.selectCarList(car);
    }

    @Override
    public int insertCar(Car car) {
        // 默认状态为可租
        if (car.getStatus() == null) {
            car.setStatus(0);
        }
        return carMapper.insertCar(car);
    }

    @Override
    public int updateCar(Car car) {
        return carMapper.updateCar(car);
    }

    @Override
    public int deleteCarById(Integer carId) {
        return carMapper.deleteCarById(carId);
    }

    @Override
    public int deleteCarByIds(Integer[] carIds) {
        return carMapper.deleteCarByIds(carIds);
    }

    @Override
    public int updateCarStatus(Integer carId, Integer status) {
        return carMapper.updateCarStatus(carId, status);
    }
}
