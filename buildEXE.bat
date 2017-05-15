@echo off
call buildJAR.bat
gradlew desktop:createEXE
