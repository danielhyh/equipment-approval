# 多阶段构建 - 第一阶段：提取分层
FROM eclipse-temurin:17-jre AS extractor


## 创建工作目录
WORKDIR /app

## 复制分层JAR文件
COPY ./target/yudao-server.jar app.jar

## 提取分层内容到目录中
RUN java -Djarmode=layertools -jar app.jar extract

# 第二阶段：运行时镜像
FROM eclipse-temurin:17-jre


## 创建工作目录
WORKDIR /yudao-server

## 从第一阶段复制分层文件
COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

## 设置 TZ 时区
ENV TZ=Asia/Shanghai

## 设置 JAVA_OPTS 环境变量，可通过 docker run -e "JAVA_OPTS=" 进行覆盖
## 优化JVM参数，提升启动和运行性能
ENV JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseG1GC -XX:+UseStringDeduplication -Djava.security.egd=file:/dev/./urandom -Dspring.backgroundpreinitializer.ignore=true"

## 应用参数
ENV ARGS=""

## 暴露后端项目的 48080 端口
EXPOSE 48090

## 使用分层启动，提升启动速度
CMD java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher $ARGS
