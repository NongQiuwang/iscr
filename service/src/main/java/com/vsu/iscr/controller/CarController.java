package com.vsu.iscr.controller;

import com.vsu.iscr.core.vo.ResultVo;
import com.vsu.iscr.domain.Car;
import com.vsu.iscr.service.CarService;
import com.vsu.iscr.utils.ResultVoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 汽车控制器
 * 
 * @author iscr
 */
@Slf4j
@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CarController {

    @Autowired
    private CarService carService;

    /**
     * 获取所有汽车列表
     */
    @GetMapping("/list")
    public ResultVo getCarList(Car car) {
        try {
            List<Car> list = carService.selectCarList(car);
            return ResultVoUtil.success(list);
        } catch (Exception e) {
            log.error("获取汽车列表失败", e);
            return ResultVoUtil.error("获取汽车列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有汽车（不带条件）
     */
    @GetMapping("/all")
    public ResultVo getAllCars() {
        try {
            List<Car> list = carService.selectAllCars();
            return ResultVoUtil.success(list);
        } catch (Exception e) {
            log.error("获取所有汽车失败", e);
            return ResultVoUtil.error("获取所有汽车失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取汽车详情
     */
    @GetMapping("/{id}")
    public ResultVo getCarById(@PathVariable("id") Integer id) {
        try {
            Car car = carService.selectCarById(id);
            if (car == null) {
                return ResultVoUtil.error("汽车不存在");
            }
            return ResultVoUtil.success(car);
        } catch (Exception e) {
            log.error("获取汽车详情失败", e);
            return ResultVoUtil.error("获取汽车详情失败：" + e.getMessage());
        }
    }

    /**
     * 新增汽车
     */
    @PostMapping
    public ResultVo addCar(@RequestBody Car car) {
        try {
            int result = carService.insertCar(car);
            if (result > 0) {
                return ResultVoUtil.success(car);
            }
            return ResultVoUtil.error("添加失败");
        } catch (Exception e) {
            log.error("添加汽车失败", e);
            return ResultVoUtil.error("添加汽车失败：" + e.getMessage());
        }
    }

    /**
     * 更新汽车信息
     */
    @PutMapping("/{id}")
    public ResultVo updateCar(@PathVariable("id") Integer id, @RequestBody Car car) {
        try {
            car.setCarId(id);
            int result = carService.updateCar(car);
            if (result > 0) {
                return ResultVoUtil.success("更新成功");
            }
            return ResultVoUtil.error("更新失败");
        } catch (Exception e) {
            log.error("更新汽车失败", e);
            return ResultVoUtil.error("更新汽车失败：" + e.getMessage());
        }
    }

    /**
     * 删除汽车
     */
    @DeleteMapping("/{id}")
    public ResultVo deleteCar(@PathVariable("id") Integer id) {
        try {
            int result = carService.deleteCarById(id);
            if (result > 0) {
                return ResultVoUtil.success("删除成功");
            }
            return ResultVoUtil.error("删除失败");
        } catch (Exception e) {
            log.error("删除汽车失败", e);
            return ResultVoUtil.error("删除汽车失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除汽车
     */
    @DeleteMapping("/batch")
    public ResultVo deleteCarBatch(@RequestBody Integer[] ids) {
        try {
            int result = carService.deleteCarByIds(ids);
            if (result > 0) {
                return ResultVoUtil.success("批量删除成功");
            }
            return ResultVoUtil.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除汽车失败", e);
            return ResultVoUtil.error("批量删除汽车失败：" + e.getMessage());
        }
    }
}
