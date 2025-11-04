FROM btgoose/jdk17:latest

# 设置工作目录
WORKDIR /app

# 复制JAR包到容器
COPY yml-server/target/dragon-dynamic-setting.jar /app/app.jar

# 设置JVM内存限制为128M（堆64m/128m，元空间64m），并加速随机源
ENV JAVA_OPTS="-Xms64m -Xmx128m -XX:MaxMetaspaceSize=64m -Djava.security.egd=file:/dev/./urandom"

# 暴露端口（应用实际监听 8080）
EXPOSE 8080

# 启动应用
CMD ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
