#!/usr/bin/env bash
mvn clean install;
mvn clean package;
docker-compose -f /data/qhcrm/app-assist/docker-compose.yml       up -d --force-recreate;