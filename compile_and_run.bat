@echo on

cd /d %~dp0

call mvn compile

call cls

call java -jar target/oop-assignment-1.0.jar

pause