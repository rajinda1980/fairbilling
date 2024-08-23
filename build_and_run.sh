#!/bin/bash

OUTPUT_FILE_PATH="./output.txt"
DOCKER_IMAGE="fairbilling:v1"

# Creating output file
echo "Creating file : $OUTPUT_FILE_PATH"
echo "" > "$OUTPUT_FILE_PATH"

# Creating Dockerfile
echo "Creating Dockerfile"
cat <<EOF > Dockerfile
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY create-file.sh /app/
RUN chmod +x create-file.sh && ./create-file.sh
COPY pom.xml .
COPY src ./src
RUN mvn clean package
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/fairbilling-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

# Creating create-file.sh
echo "Creating create-file.sh"
cat <<EOF > create-file.sh
#!/bin/bash
echo "File should already exist outside the container"
EOF

# Ensure create-file.sh has executable permissions
chmod +x create-file.sh

# Building the Docker image
echo "Building the Docker image: $DOCKER_IMAGE"
docker build -t "$DOCKER_IMAGE" .

# Running Docker container and mounting volumes
echo "Running Docker container and mounting $OUTPUT_FILE_PATH to /app/output.txt"
sudo docker run -v "$(pwd)/logfile.log:/logfile.log" -v "$(pwd)/$OUTPUT_FILE_PATH:/app/output.txt" "$DOCKER_IMAGE" "/logfile.log"

# Cleanup
rm Dockerfile create-file.sh
