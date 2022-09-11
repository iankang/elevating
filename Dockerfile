FROM maven:3.8.4-openjdk-11 as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
ADD . $HOME
RUN mvn clean package -DskipTests

FROM openjdk:11
VOLUME /tmp
EXPOSE 8092
COPY --from=build /usr/app/target/elevating-0.0.1.jar elevating.jar
ENTRYPOINT ["java", "-jar","elevating.jar"]