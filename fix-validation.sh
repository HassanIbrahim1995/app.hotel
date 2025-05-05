#!/bin/bash
sed -i 's/throw new ValidationErrorResponse/throw ValidationErrorResponse.exception/g' api-module/src/main/java/com/shiftmanager/api/service/impl/ManagerServiceImpl.java
