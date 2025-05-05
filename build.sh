#!/bin/bash

echo "Building parent project..."
mvn clean install -N

echo "Building core-module..."
cd core-module
mvn clean install -DskipTests
cd ..

echo "Building employee-module..."
cd employee-module
mvn clean install -DskipTests
cd ..

echo "Building manager-module..."
cd manager-module
mvn clean install -DskipTests
cd ..

echo "Building shift-module..."
cd shift-module
mvn clean install -DskipTests
cd ..

echo "Building schedule-module..."
cd schedule-module
mvn clean install -DskipTests
cd ..

echo "Building vacation-module..."
cd vacation-module
mvn clean install -DskipTests
cd ..

echo "Building reporting-module..."
cd reporting-module
mvn clean install -DskipTests
cd ..

echo "Building notification-module..."
cd notification-module
mvn clean install -DskipTests
cd ..

echo "Building api-module..."
cd api-module
mvn clean install -DskipTests
cd ..

echo "All modules built successfully"
