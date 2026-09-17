# ISCR — Car Rental Information System

汽车租赁信息系统（Information System of Car Rental）。这是一个前后端分离的课程项目，包含基于 Spring Boot 的 REST API 后端和基于 Vue 3 的 Web 客户端。

## 功能

- 用户注册、登录、退出
- 车辆列表与详情浏览、后台车辆增删改查、图片上传
- 在线预约、取消预约、预约状态管理
- 后台仪表盘、车辆管理、预约管理、用户管理
- 表单输入校验与错误提示
- 基于角色的页面访问控制（普通用户 / 管理员）
- 密码 BCrypt 加密存储
- 浏览器 LocalStorage 本地登录态

## 技术栈

### 后端（`service`）

- Java 8
- Spring Boot 2.4.1
- MyBatis 2.2.0
- MySQL 8
- Druid 连接池
- Maven

### 前端（`web-page`）

- Vue 3
- Vue Router 4
- Pinia
- Element Plus
- Axios
- Vite

## 项目结构

```
.
├── service/                # 后端 Spring Boot
│   └── src/
│       ├── main/java/com/vsu/iscr/
│       │   ├── controller/  # REST 控制器
│       │   ├── service/     # 业务逻辑（接口 + 实现）
│       │   ├── mapper/      # MyBatis 数据访问
│       │   ├── domain/      # 实体类
│       │   ├── core/        # 统一返回对象、配置
│       │   └── utils/       # 工具类
│       └── main/resources/
│           ├── mapping/     # Mapper XML
│           ├── sql/         # 数据库脚本
│           └── application*.yml
└── web-page/               # 前端 Vue 3
    └── src/
        ├── api/            # 接口封装
        ├── router/         # 路由与守卫
        ├── utils/          # Axios 实例与拦截器
        ├── layouts/        # 布局
        └── views/          # 页面（portal / auth / admin）
```

## 环境要求

- JDK 8 或更高
- MySQL 8
- Maven 3.6 或更高
- Node.js `^20.19.0` 或 `>=22.12.0`

## 数据库

数据库初始化脚本位于 `service/src/main/resources/sql/iscr.sql`，会创建数据库 `iscr` 和三张表：

- `tb_users` — 用户
- `tb_car` — 车辆
- `tb_reservation` — 预约

导入脚本：

```bash
mysql -uroot -p < service/src/main/resources/sql/iscr.sql
```

默认数据库连接配置在 `service/src/main/resources/application-dev.yml`：

```yaml
url: jdbc:mysql://localhost:3306/iscr
username: root
password: 123456
```

> 请根据本机环境修改账号密码。

## 后端启动

```bash
cd service
mvn spring-boot:run
```

也可以在 IntelliJ IDEA 中直接运行 `IscrApplication`。

后端地址：<http://localhost:8080/iscr>

## 前端启动

```bash
cd web-page
npm install
npm run dev
```

前端地址：<http://localhost:5173>

前端通过 `.env.development` 中的 `VITE_API_BASE_URL=http://localhost:8080/iscr` 访问后端。

## 默认账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |
| 普通用户 | `user1` | `123456` |

## API 概览

统一返回格式 `ResultVo`：`code`、`message`、`data`、`count`。`code = 0` 表示成功，`code = -1` 表示业务失败。

### 认证 `/api/auth`

- `POST /login` — 登录
- `POST /register` — 注册
- `POST /logout` — 退出

### 车辆 `/api/cars`

- `GET /all` — 获取全部车辆
- `GET /list` — 按条件查询车辆
- `GET /{id}` — 获取单个车辆
- `POST /` — 新增车辆
- `PUT /{id}` — 更新车辆
- `DELETE /{id}` — 删除车辆
- `DELETE /batch` — 批量删除车辆

### 预约 `/api/reservations`

- `GET /all` — 获取全部预约
- `GET /list` — 按条件查询预约
- `GET /{id}` — 获取单个预约
- `GET /user/{userId}` — 按用户查询预约
- `POST /` — 创建预约
- `PUT /{id}` — 更新预约
- `PATCH /{id}/status` — 更新预约状态
- `DELETE /{id}` — 取消预约（软删除）

### 用户 `/api/users`

- `GET /all` — 获取全部用户
- `GET /{id}` — 获取单个用户
- `PUT /{id}` — 更新用户
- `DELETE /{id}` — 删除用户

### 上传 `/api/upload`

- `POST /car-image` — 上传车辆图片

## 测试

后端自动化测试使用 MockMvc：

```bash
cd service
mvn test
```

## 构建

后端：

```bash
cd service
mvn package
```

前端：

```bash
cd web-page
npm run build
```

## 安全提示

`application-dev.yml` 中包含了数据库明文密码，部署到公开环境前请改用环境变量或配置占位符，避免泄露敏感信息。
