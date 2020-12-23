FROM openjdk:8
ADD build/libs/drunker-filestorage.jar drunker-filestorage.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "drunker-filestorage.jar"]