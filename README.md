# mms-doc-convert

This repository contains a Spring Boot application for document conversion from HTML to multiple formats. A Dockerfile is provided to build an image with all dependencies built in.

## How to run:

Copy src/main/resources/application.properties.example to application.properties and change accordingly

In the top level directory run `./gradlew bootRun`

## How to build a Docker image:

To build the docker image, clone the repository and change application.properties accordingly, and in the top level directory run `./gradlew buildDocker`

The Dockerfile and example properties supplied uses weasyprint as the default pdf engine, to use PrinceXML the Dockerfile and properties needs to be changed accordingly

To run the built docker image do `docker run -p 8080:8080 mms-doc-convert:latest`

Go to http://localhost:8080/swagger-ui.html for usage
