@echo on

cd /d %~dp0

call mvn -T 1C package

call cls

call java -jar target/oop-assignment-1.0.jar

pause