FROM openjdk:11

COPY ./target/yumarket.jar /application.jar

ENTRYPOINT ["java","-jar","/application.jar"]

EXPOSE 8080
