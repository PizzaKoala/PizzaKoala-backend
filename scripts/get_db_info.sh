#!/bin/bash

# Ensure environment variables are set
if [ -z "$RDS_HOSTNAME" ] || [ -z "$RDS_PORT" ] || [ -z "$RDS_DB_NAME" ] || [ -z "$RDS_USERNAME" ] || [ -z "$RDS_PASSWORD" ]; then
  echo "Environment variables are not set."
  exit 1
fi

# Print the MySQL connection URL
echo "jdbc:mysql://${RDS_HOSTNAME}:${RDS_PORT}/${RDS_DB_NAME}"
echo "Username: ${RDS_USERNAME}"
echo "Password: ${RDS_PASSWORD}"
