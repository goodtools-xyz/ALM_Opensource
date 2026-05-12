
# 使用Maven镜像进行构建
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# 复制pom.xml并下载依赖
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 编译打包
RUN mvn clean package -DskipTests

# 使用JRE镜像运行
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 从构建阶段复制jar包
COPY --from=builder /app/target/alm-1.0.0.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai

# 暴露端口
EXPOSE 8080

# 运行应用
CMD ["java", "-jar", "app.jar"]
