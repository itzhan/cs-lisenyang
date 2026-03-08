@echo off
chcp 65001 >nul 2>&1
title 翔泳社游泳馆管理系统 · 一键启动
color 0F

:: ============================================================
:: 翔泳社游泳馆管理系统 · Windows 一键启动脚本
:: ============================================================

set PROJECT_DIR=%~dp0
set BACKEND_PORT=8080
set FRONTEND_PORT=5173
set ADMIN_PORT=5174

echo.
echo ========================================================
echo    🏊 翔泳社游泳馆管理系统 · Windows 启动脚本
echo ========================================================
echo.

:: ============================================================
:: 1. 检查依赖
:: ============================================================
echo 🔍 检查运行环境...
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo    ❌ 未检测到 Java，请先安装 JDK 17+
    pause
    exit /b 1
) else (
    echo    ✅ Java 已安装
)

where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo    ❌ 未检测到 Maven，请先安装 Maven
    pause
    exit /b 1
) else (
    echo    ✅ Maven 已安装
)

where node >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo    ❌ 未检测到 Node.js，请先安装 Node.js
    pause
    exit /b 1
) else (
    echo    ✅ Node.js 已安装
)

where pnpm >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo    ⚠️  未检测到 pnpm，正在安装...
    npm install -g pnpm
) else (
    echo    ✅ pnpm 已安装
)

:: ============================================================
:: 2. 清理端口占用
:: ============================================================
echo.
echo 🔌 清理端口占用...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%BACKEND_PORT% ^| findstr LISTENING 2^>nul') do (
    taskkill /F /PID %%a >nul 2>&1
    echo    ⚠️  已清理端口 %BACKEND_PORT% 占用
)
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%FRONTEND_PORT% ^| findstr LISTENING 2^>nul') do (
    taskkill /F /PID %%a >nul 2>&1
    echo    ⚠️  已清理端口 %FRONTEND_PORT% 占用
)
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%ADMIN_PORT% ^| findstr LISTENING 2^>nul') do (
    taskkill /F /PID %%a >nul 2>&1
    echo    ⚠️  已清理端口 %ADMIN_PORT% 占用
)
echo    ✅ 端口已就绪

:: ============================================================
:: 3. 检查前端依赖
:: ============================================================
echo.
echo 📦 检查前端依赖...
if not exist "%PROJECT_DIR%frontend\node_modules" (
    echo    📥 正在安装前端依赖...
    cd /d "%PROJECT_DIR%frontend"
    pnpm install
    echo    ✅ 前端依赖安装完成
) else (
    echo    ✅ 前端依赖已就绪
)

echo.
echo 📦 检查管理端依赖...
if not exist "%PROJECT_DIR%arco-design-pro-vue\arco-design-pro-vite\node_modules" (
    echo    📥 正在安装管理端依赖...
    cd /d "%PROJECT_DIR%arco-design-pro-vue\arco-design-pro-vite"
    pnpm install
    echo    ✅ 管理端依赖安装完成
) else (
    echo    ✅ 管理端依赖已就绪
)

:: ============================================================
:: 4. 启动后端（新窗口 - 绿色文字）
:: ============================================================
echo.
echo 🚀 启动服务...
echo    ▶ 启动后端 (Spring Boot)...
start "🏊 后端 - Spring Boot" cmd /k "cd /d %PROJECT_DIR%swim-admin && color 0A && echo ====== 后端服务启动中 ====== && mvn spring-boot:run"

:: ============================================================
:: 5. 启动前端（新窗口 - 蓝色文字）
:: ============================================================
echo    ▶ 启动前端 (Vite)...
start "🏊 前端 - Vite" cmd /k "cd /d %PROJECT_DIR%frontend && color 0B && echo ====== 前端服务启动中 ====== && pnpm dev"

:: ============================================================
:: 6. 启动管理端（新窗口 - 黄色文字）
:: ============================================================
echo    ▶ 启动管理端 (Arco Design Pro)...
start "🏊 管理端 - Arco" cmd /k "cd /d %PROJECT_DIR%arco-design-pro-vue\arco-design-pro-vite && color 0E && echo ====== 管理端服务启动中 ====== && pnpm dev -- --port %ADMIN_PORT%"

:: ============================================================
:: 7. 打印角色信息
:: ============================================================
echo.
echo ========================================================
echo    🏊 翔泳社游泳馆管理系统 · 启动成功
echo ========================================================
echo.
echo    📡 服务地址：
echo       后端 API:  http://localhost:%BACKEND_PORT%
echo       前端页面:  http://localhost:%FRONTEND_PORT%
echo       管理后台:  http://localhost:%ADMIN_PORT%
echo.
echo    👤 测试账号：
echo    ┌────────────┬────────────┬────────────┬──────────────┐
echo    │  角色      │  用户名    │  密码      │  姓名        │
echo    ├────────────┼────────────┼────────────┼──────────────┤
echo    │  管理员    │  admin     │  admin123  │  系统管理员  │
echo    │  管理员    │  admin02   │  admin123  │  王晓琳      │
echo    │  教练      │  coach01   │  coach123  │  李明辉      │
echo    │  教练      │  coach02   │  coach123  │  王海燕      │
echo    │  教练      │  coach03   │  coach123  │  赵文博      │
echo    │  教练      │  coach04   │  coach123  │  陈思怡      │
echo    │  教练      │  coach05   │  coach123  │  孙志强      │
echo    │  用户      │  user01    │  user123   │  张伟        │
echo    │  用户      │  user02    │  user123   │  刘婷        │
echo    │  用户      │  user03    │  user123   │  周浩然      │
echo    │  用户      │  user04    │  user123   │  吴佳琪      │
echo    │  用户      │  user05    │  user123   │  郑凯文      │
echo    │  用户      │  user06    │  user123   │  黄思涵      │
echo    │  用户      │  user07    │  user123   │  林子轩      │
echo    │  用户      │  user08    │  user123   │  杨雨萱      │
echo    └────────────┴────────────┴────────────┴──────────────┘
echo.
echo    💡 提示: 后端、前端和管理端已在独立窗口中运行
echo    💡 关闭对应窗口即可停止服务
echo ========================================================
echo.
pause
