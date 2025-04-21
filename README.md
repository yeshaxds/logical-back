# 后端业务逻辑与数据库应用示例项目

本项目是一个基于Spring Boot的任务管理系统，用于展示后端业务逻辑和数据库应用。

## 功能特点

- 完整的实体关系模型（用户、任务、分类、标签）
- 数据校验和异常处理
- RESTful API设计
- 服务层和控制器层分离
- 使用JPA/Hibernate进行数据访问
- 高级数据库查询和统计功能
- 事务管理和数据一致性
- H2内存数据库用于开发和测试

## 技术栈

- Java 11
- Spring Boot 2.7.15
- Spring Data JPA
- Hibernate
- H2数据库
- Lombok
- Maven

## 项目结构

```
src/main/java/com/example/logicalback/
├── LogicalBackApplication.java     # 应用入口
├── config/                         # 配置类
│   └── DataInitializer.java        # 初始化示例数据
├── controller/                     # 控制器层
│   ├── UserController.java         # 用户相关API
│   ├── TaskController.java         # 任务相关API
│   └── StatisticsController.java   # 统计相关API
├── dto/                            # 数据传输对象
│   ├── UserDTO.java                # 用户DTO
│   ├── TaskDTO.java                # 任务DTO
│   ├── CategoryDTO.java            # 分类DTO
│   └── TagDTO.java                 # 标签DTO
├── exception/                      # 异常处理
│   ├── ResourceNotFoundException.java   # 资源未找到异常
│   └── GlobalExceptionHandler.java      # 全局异常处理
├── model/                          # 实体模型
│   ├── User.java                   # 用户实体
│   ├── Task.java                   # 任务实体  
│   ├── Category.java               # 分类实体
│   └── Tag.java                    # 标签实体
├── repository/                     # 数据访问层
│   ├── UserRepository.java         # 用户仓库
│   ├── TaskRepository.java         # 任务仓库
│   ├── CategoryRepository.java     # 分类仓库
│   └── TagRepository.java          # 标签仓库
└── service/                        # 服务层
    ├── UserService.java            # 用户服务接口
    ├── TaskService.java            # 任务服务接口
    ├── StatisticsService.java      # 统计服务接口
    └── impl/                       # 服务实现
        ├── UserServiceImpl.java    # 用户服务实现
        ├── TaskServiceImpl.java    # 任务服务实现
        └── StatisticsServiceImpl.java # 统计服务实现
```

## 快速开始

1. 克隆项目
2. 使用Maven构建：`mvn clean package`
3. 运行应用：`java -jar target/logical-back-0.0.1-SNAPSHOT.jar`
4. 应用默认在 http://localhost:8080 启动

## API文档

- 用户API: `/api/users`
- 任务API: `/api/tasks`
- 统计API: `/api/statistics`

## H2数据库控制台

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- 用户名: `sa`
- 密码: `password`
