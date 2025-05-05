#!/bin/bash

echo "Starting Shift Management System application..."
cd shift-management-system
mvn clean install -DskipTests
cd app-module
mvn spring-boot:run