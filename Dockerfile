FROM eclipse-temurin:17-jdk-jammy as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# clean up the file
RUN sed -i 's/\r$//' mvnw
# run with the SH path
RUN /bin/sh mvnw dependency:resolve
COPY src ./src

ARG aws_access_key_id
ARG aws_secret_access_key

ENV AWS_ACCESS_KEY_ID $aws_access_key_id
ENV AWS_SECRET_ACCESS_KEY $aws_secret_access_key

FROM base as test
RUN ["./mvnw", "test"]

FROM base as build
RUN ./mvnw package

FROM eclipse-temurin:17-jre-jammy as production
EXPOSE 8081
COPY --from=build /app/target/data-object.jar /data-object.jar
CMD ["java","-jar", "/data-object.jar"]