# Use a base image with JDK (adapt version as needed)
FROM maven:3.8.4-openjdk-17

# Copy your source code into the Docker image
COPY . /usr/src/myapp

# Set the working directory to your app directory
WORKDIR /usr/src/myapp

# By default, run Maven test
CMD ["mvn", "test"]
