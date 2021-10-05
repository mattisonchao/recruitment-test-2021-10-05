FROM openjdk:8-jre
WORKDIR /usr/local/app
ADD target/comments-tree-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7000
VOLUME /home/comments-tree/logs
ENTRYPOINT ["java", "-jar"]
CMD ["app.jar"]