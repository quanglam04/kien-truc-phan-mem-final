# Giai đoạn 1: Build (Sử dụng Maven để đóng gói)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Giai đoạn 2: Chạy (Sử dụng JRE nhẹ để vận hành)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Chỉ copy duy nhất file .jar từ giai đoạn build sang
COPY --from=build /app/target/*.jar app.jar
# Chạy trực tiếp bằng lệnh java -jar
ENTRYPOINT ["java", "-jar", "app.jar"]