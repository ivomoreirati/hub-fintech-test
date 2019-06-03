# Base Image
FROM java:8

# Add the fatjar in the image
COPY target/hubfintech-1.0-SNAPSHOT.jar /

# Default command
CMD java -jar /hubfintech-1.0-SNAPSHOT.jar
