@echo off
docker-compose -f compose.yml -f compose.dev.yml up --build --watch
if errorlevel 1 pause
