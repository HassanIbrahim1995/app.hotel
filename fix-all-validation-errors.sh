#!/bin/bash
# Replace all instances of "throw new ValidationErrorResponse" with "throw ValidationErrorResponse.exception"
find api-module -name "*.java" -type f -exec sed -i 's/throw new ValidationErrorResponse/throw ValidationErrorResponse.exception/g' {} \;
