@echo off

cd /d %~dp0

mvn clean

mvn -T 1C package