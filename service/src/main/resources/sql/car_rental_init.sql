-- 汽车租赁系统数据库初始化脚本

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS iscr DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE iscr;

-- 用户表（如果表已存在，请手动添加 role、create_time、update_time 字段）
CREATE TABLE IF NOT EXISTS `tb_users` (
  `u_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `user_name` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `email` VARCHAR(100) COMMENT '邮箱',
  `password` VARCHAR(200) NOT NULL COMMENT '密码（加密）',
  `phone` VARCHAR(20) COMMENT '手机号',
  `role` INT DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 汽车表
CREATE TABLE IF NOT EXISTS `tb_car` (
  `car_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '汽车ID',
  `brand` VARCHAR(50) NOT NULL COMMENT '品牌',
  `series` VARCHAR(50) NOT NULL COMMENT '车系',
  `year` VARCHAR(20) COMMENT '年款',
  `model` VARCHAR(100) COMMENT '型号',
  `category` VARCHAR(50) COMMENT '类别（如中大型车）',
  `displacement` VARCHAR(20) COMMENT '排量（如2.0T）',
  `box_count` VARCHAR(20) COMMENT '厢数',
  `seat_count` INT COMMENT '座位数',
  `transmission` VARCHAR(20) COMMENT '变速箱（自动/手动）',
  `door_count` INT COMMENT '车门数',
  `fuel_type` VARCHAR(20) COMMENT '燃料类型（汽油/柴油/电动）',
  `fuel_label` VARCHAR(20) COMMENT '燃料标号（如95号）',
  `tank_capacity` VARCHAR(20) COMMENT '油箱容积（如73L）',
  `maintenance_mileage` VARCHAR(50) COMMENT '养护里程（如5000公里）',
  `daily_rent` DECIMAL(10,2) DEFAULT 0.00 COMMENT '日租金',
  `status` INT DEFAULT 0 COMMENT '状态：0-可租，1-已租，2-维护中',
  `image_path` VARCHAR(500) COMMENT '图片路径',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='汽车表';

-- 预约表
CREATE TABLE IF NOT EXISTS `tb_reservation` (
  `reservation_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '预约ID',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `car_id` INT NOT NULL COMMENT '汽车ID',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NOT NULL COMMENT '结束日期',
  `total_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '总价',
  `status` INT DEFAULT 0 COMMENT '状态：0-待确认，1-已确认，2-已取消，3-已完成',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `tb_users`(`u_id`) ON DELETE CASCADE,
  FOREIGN KEY (`car_id`) REFERENCES `tb_car`(`car_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 插入管理员用户（密码：admin123，实际项目应该加密）
INSERT INTO `tb_users` (`user_name`, `email`, `password`, `phone`, `role`) 
VALUES ('admin', 'admin@example.com', 'admin123', '13800138000', 1)
ON DUPLICATE KEY UPDATE `user_name`='admin';

-- 插入测试用户
INSERT INTO `tb_users` (`user_name`, `email`, `password`, `phone`, `role`) 
VALUES ('user1', 'user1@example.com', '123456', '13800138001', 0)
ON DUPLICATE KEY UPDATE `user_name`='user1';

-- 插入示例汽车数据
INSERT INTO `tb_car` (`brand`, `series`, `year`, `model`, `category`, `displacement`, 
  `box_count`, `seat_count`, `transmission`, `door_count`, `fuel_type`, `fuel_label`, 
  `tank_capacity`, `maintenance_mileage`, `daily_rent`, `status`) VALUES
('Audi', 'A6L', '2024 Model', '45 TFSI quattro Luxury Dynamic', 'Mid-Large Sedan', '2.0T',
  'threeCompartment', 5, 'automatic', 4, 'Gasoline', '95 Octane', '73L', '5000KM', 500.00, 0),
('BMW', '5 Series', '2024 Model', '530Li Exclusive M Sport', 'Mid-Large Sedan', '2.0T',
  'threeCompartment', 5, 'automatic', 4, 'Gasoline', '95 Octane', '68L', '10000KM', 550.00, 0),
('Mercedes-Benz', 'E-Class', '2024 Model', 'E 300 L Luxury', 'Mid-Large Sedan', '2.0T',
  'threeCompartment', 5, 'automatic', 4, 'Gasoline', '95 Octane', '66L', '10000KM', 600.00, 0),
('Tesla', 'Model 3', '2024 Model', 'RWD Edition', 'Mid-size Sedan', 'Electric',
  'threeCompartment', 5, 'automatic', 4, 'Electric', '-', '-', '20000KM', 400.00, 0),
('Toyota', 'Camry', '2024 Model', '2.0E Elite Edition', 'Mid-size Sedan', '2.0L',
  'threeCompartment', 5, 'automatic', 4, 'Gasoline', '92 Octane', '60L', '5000KM', 300.00, 0);
