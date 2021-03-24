## Inventory Management System

This project has a basic implementation of adding new product catalog 
in the inventory management system.

Features:
1. CRUD operation to create Product
2. Ability to query the Product catalog based on any attributes.
3. Ability to import the entire Product catalog from an input CSV file.

### Pre-requisites
    a. JDK 11
    b. maven 3.6 +
    c. curl/postman/web-browser

### Building and Running using Docker
If developer do not have or want to install pre-requisites we can alternatvely build and run using docker. Steps are below :
1. Get docker installed https://docs.docker.com/get-docker/.
2. docker pull maven:3.6.3-jdk-11
3. docker run -p 8080:8080 -v `pwd`:/IMS -it maven:3.6.3-jdk-11 /bin/bash

Once you're logged in to the terminal follow, regular maven commands highlighted below.

### Build
    mvn clean package

### Run
    java -jar target/IMS-1.0-SNAPSHOT.jar 

### Swagger-ui
Once the application is run, developer can access the swagger-ui at http://localhost:8080/swagger-ui/.
It aims to provide details of the API.

### Specification for importing CSV file
The CSV used for importing should follow the following criteria :
1. Should have first row as header.
2. Should use ',' as delimiter.
3. Make sure the importing CSV do not of extra space in the headerNames/ values.
4. Following are the headers the import service is looking for :
   1. partName
   2. manufacturer
   3. cost
   4. productCategory
    
The service can handle the file with above headers in any order.    

A sample csv for reference is placed in src/main/resources/sample.csv.

### Configuration
The service can be configured for following :
1. upload store: It is a path or directory where the csv files uploaded are stored.
2. max file size allowed for import.

