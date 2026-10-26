#Here maven downloads the libraries and builds the application, then the final image is built with only the JRE and the application jar file.
FROM maven:3.9-eclipse-temurin-17 AS builder

#creats the app folder insode the image and sets it as the working directory
WORKDIR /app

#copies the pom file to the working directory and downloads the dependencies to the local repository
COPY pom.xml .
RUN mvn dependency:go-offline -B   #downloads all the libraries listed in pom.xml

#copying the source code to the working directory and building the application, skipping the tests
COPY src ./src
RUN mvn clean package -DskipTests -B #compiles the code and packages it to a runnable jar file, skipping the tests


FROM eclipse-temurin:17-jre

WORKDIR /app

#Takes the built jar and saves it as app.jar in the new image.
COPY --from=builder /app/target/*.jar app.jar


EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
