@echo on

cd /d %~dp0

call mvn package

call cls

call java -jar target/oop-assignment-1.0-jar-with-dependencies.jar

pause