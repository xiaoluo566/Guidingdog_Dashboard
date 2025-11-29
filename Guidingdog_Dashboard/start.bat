@echo off
echo ========================================
echo 导盲犬仪表盘启动脚本
echo Guiding Dog Dashboard Startup
echo ========================================
echo.

echo [1/3] 检查数据库连接...
echo 请确保MySQL已启动并且GuidingDog_Dashboard数据库已创建
echo.

echo [2/3] 启动Spring Boot应用...
echo.

call mvnw.cmd spring-boot:run

pause

