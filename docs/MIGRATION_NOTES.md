# 迁移说明：天气微服务 → 单体教学版

**原项目**：https://github.com/Yik11Yueuii/weather-system

## 保留与转化

| 原项目 | 新项目 | 说明 |
|---|---|---|
| `hanshidemo-weather-citylist9300` | `city/CityCatalog` + `web/CityController` | 城市功能保留只读列表；改成内置城市，不要求 MySQL |
| `hanshidemo-weather-querydata9200` | `weather/WeatherService` + `web/WeatherController` | 天气业务合并到同一个 Spring 容器 |
| 天行天气 API | `OpenMeteoWeatherDataSource` | 用免密钥的 Open-Meteo 替代旧API，返回自定义稳定模型 |
| Redis 两小时缓存 | `WeatherService` 的进程内缓存 | 改为默认10分钟，先理解缓存，再学 Redis |
| Vue 3 前端 | `resources/static` HTML/CSS/JS | 单端口交付，降低初学者环境复杂度 |
| Eureka、Zuul、Feign、Hystrix | 移除 | 单体模式不需要服务发现、微服务网关和服务间RPC |
| 定时缓存刷新模块 | 暂未迁移 | 后续可按需要练习 `@Scheduled` |
| AI聊天、动态效果、城市维护接口 | 暂未迁移 | 非第一阶段核心目标，不应声称功能等价 |

## 一次请求现在怎么走？

```text
Browser GET /api/weather/101020100
   ↓
WeatherController
   ↓
WeatherService [确认城市 + 查内存缓存]
   ↓（缓存未命中）
WeatherDataSource [demo or live]
   ↓
WeatherReport JSON → Browser
```

## 为什么不是在原仓库直接修改？

原项目包含 Java 8 / Spring Boot 2.2 / 多模块 Spring Cloud 代码，适合后续拿来比较。教学版采用 Java 21 与 Boot 3.5 的单模块 Maven，避免损坏原有微服务及历史。**迁移的是业务能力与学习目标，不是原代码的严格一对一等价转换。**
