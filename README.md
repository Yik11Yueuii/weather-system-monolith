# Weather System Monolith · 天气系统单体学习版

这是从原仓库 **[weather-system](https://github.com/Yik11Yueuii/weather-system)** 的核心业务重新整理的**独立教学版**。它不是微服务工程整体代码的原样搬运，也不声称完整保留原项目的 AI 聊天、Redis、MySQL 和定时刷新功能。

**目标：** 用一个可运行的 Java 21 + Spring Boot 3.5.16 应用，学习从 Controller 到 Service 再到数据来源的完整请求链路。代码是学习对照样本；真正练习时请在新目录里自己手敲，不要直接复制。

## 功能

- 城市列表查询（8座预置城市）；
- 选择城市，查看当前温度、湿度、天气与7日预报；
- 默认离线演示模式，明确标注「演示数据」，**不是实时预报**；
- 可切换到 Open-Meteo 公开天气 API，无需密钥（依赖网络）；
- 10分钟进程内缓存、基本的 API 异常响应；
- 一页纯 HTML/CSS/JS 前端，由 Spring Boot 在同一端口提供，无 Node.js 构建步骤；
- JUnit 5 + MockMvc 的少量教学用测试。

## 环境要求

- Java 21
- Maven 3.9+，或 IntelliJ IDEA 自带的 Maven 功能
- **不需要** MySQL、Redis、Eureka、Zuul、Node.js 和第三方 API Key

## 运行

在仓库根目录运行：

```bash
mvn spring-boot:run
```

访问 http://localhost:8080 ，或者接口：

```text
GET http://localhost:8080/api/health
GET http://localhost:8080/api/cities
GET http://localhost:8080/api/weather/101020100
```

演示模式默认启用。想看真实预报（Windows PowerShell）：

```powershell
$env:WEATHER_MODE="live"
mvn spring-boot:run
```

其他终端（macOS/Linux）：

```bash
WEATHER_MODE=live mvn spring-boot:run
```

**真实天气数据**由 [Open-Meteo](https://open-meteo.com/en/docs) 提供。用户应遵守该服务的许可与使用条款；演示版不宜直接部署成商用服务。需要联网；网络或第三方API出错时返回 `502`，不会伪装成真实数据。`WEATHER_CACHE_SECONDS` 可设置缓存秒数，0表示不缓存。配置见 `src/main/resources/application.yml`。

## 单体目录结构

```text
src/main/java/dev/yik/weather/
  WeatherMonolithApplication.java    # 唯一启动入口
  city/                             # 城市实体与城市列表
  weather/                          # 天气模型、天气来源、Service与缓存
  web/                              # HTTP接口及错误处理
src/main/resources/static/          # 同端口网页
src/test/                            # API与Service测试
```

## API 约定

- `GET /api/cities` → 城市数组
- `GET /api/weather/{cityId}` → 天气对象（demo / open-meteo）
- `GET /api/health` → `{ "status": "UP" }`
- 无效城市 → HTTP 404，`CITY_NOT_FOUND`
- 天气源故障 → HTTP 502，`WEATHER_UNAVAILABLE`

## 测试

推送到 GitHub 后，`.github/workflows/ci.yml` 会自动运行 Maven 测试（演示模式、无需真实天气 API）。

```bash
mvn test
```

## 接下来如何手敲学习

请从 [docs/LEARNING_PATH.md](docs/LEARNING_PATH.md) 的「第1课」开始，**不要直接边看整个源码边照抄**。老师每次只讲一个知识点，自己写完再与参考版本对照。旧版微服务如何映射见 [docs/MIGRATION_NOTES.md](docs/MIGRATION_NOTES.md)。

> 注意：该仓库代码以教学可读性为首要目标，不是高并发/商用天气平台；缓存、并发、API容错与存储能力后续可逐步增强。
