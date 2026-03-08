#!/bin/bash
# ============================================================
# 翔泳社游泳馆管理系统 · Mac 一键启动脚本
# ============================================================
set -e

# ---------- 颜色定义 ----------
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
BOLD='\033[1m'
NC='\033[0m' # No Color

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
DB_NAME="xiangyongshe"
DB_USER="root"
DB_PASS="ab123168"
BACKEND_PORT=8080
FRONTEND_PORT=5173
ADMIN_PORT=5174

# ---------- PID 文件目录 ----------
LOG_DIR="$PROJECT_DIR/.logs"
mkdir -p "$LOG_DIR"

# ============================================================
# 清理函数：Ctrl+C 终止时杀掉后台进程
# ============================================================
cleanup() {
    echo ""
    echo -e "${YELLOW}⏹  正在停止所有服务...${NC}"
    if [ -f "$LOG_DIR/backend.pid" ]; then
        kill $(cat "$LOG_DIR/backend.pid") 2>/dev/null && echo -e "${GREEN}✅ 后端已停止${NC}" || true
        rm -f "$LOG_DIR/backend.pid"
    fi
    if [ -f "$LOG_DIR/frontend.pid" ]; then
        kill $(cat "$LOG_DIR/frontend.pid") 2>/dev/null && echo -e "${GREEN}✅ 前端已停止${NC}" || true
        rm -f "$LOG_DIR/frontend.pid"
    fi
    if [ -f "$LOG_DIR/admin.pid" ]; then
        kill $(cat "$LOG_DIR/admin.pid") 2>/dev/null && echo -e "${GREEN}✅ 管理端已停止${NC}" || true
        rm -f "$LOG_DIR/admin.pid"
    fi
    # 额外清理端口占用
    lsof -ti :$BACKEND_PORT | xargs kill -9 2>/dev/null || true
    lsof -ti :$FRONTEND_PORT | xargs kill -9 2>/dev/null || true
    lsof -ti :$ADMIN_PORT | xargs kill -9 2>/dev/null || true
    echo -e "${GREEN}🛑 所有服务已关闭，再见！${NC}"
    exit 0
}
trap cleanup SIGINT SIGTERM

# ============================================================
# 1. 依赖检查
# ============================================================
echo -e "${CYAN}${BOLD}🔍 检查运行环境...${NC}"
MISSING=0
for cmd in java mvn node pnpm mysql; do
    if ! command -v $cmd &> /dev/null; then
        echo -e "  ${RED}❌ 缺少依赖: $cmd${NC}"
        MISSING=1
    else
        echo -e "  ${GREEN}✅ $cmd 已安装${NC}"
    fi
done

if [ $MISSING -eq 1 ]; then
    echo -e "${RED}请先安装缺少的依赖后再运行此脚本！${NC}"
    echo -e "${YELLOW}提示: 可使用 brew install maven node pnpm mysql 进行安装${NC}"
    exit 1
fi

# ============================================================
# 2. 检查并启动 MySQL 服务
# ============================================================
echo ""
echo -e "${CYAN}${BOLD}🗄️  检查 MySQL 服务状态...${NC}"
if ! mysqladmin ping -u "$DB_USER" -p"$DB_PASS" --silent 2>/dev/null; then
    echo -e "  ${YELLOW}⚠️  MySQL 未启动，正在尝试启动...${NC}"
    brew services start mysql 2>/dev/null || mysql.server start 2>/dev/null || {
        echo -e "  ${RED}❌ 无法自动启动 MySQL，请手动启动后重试！${NC}"
        exit 1
    }
    sleep 3
    echo -e "  ${GREEN}✅ MySQL 已启动${NC}"
else
    echo -e "  ${GREEN}✅ MySQL 服务运行中${NC}"
fi

# ============================================================
# 3. 数据库导入（若已存在则跳过）
# ============================================================
echo ""
echo -e "${CYAN}${BOLD}📦 检查数据库...${NC}"
DB_EXISTS=$(mysql -u "$DB_USER" -p"$DB_PASS" -e "SHOW DATABASES LIKE '$DB_NAME';" 2>/dev/null | grep "$DB_NAME" || true)

if [ -z "$DB_EXISTS" ]; then
    echo -e "  ${YELLOW}📥 数据库 $DB_NAME 不存在，正在导入...${NC}"
    mysql -u "$DB_USER" -p"$DB_PASS" < "$PROJECT_DIR/swim-admin/src/main/resources/db/schema.sql"
    echo -e "  ${GREEN}  ✅ 表结构导入完成${NC}"
    mysql -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" < "$PROJECT_DIR/swim-admin/src/main/resources/db/data.sql"
    echo -e "  ${GREEN}  ✅ 测试数据导入完成${NC}"
    echo -e "  ${GREEN}✅ 数据库初始化完成！${NC}"
else
    echo -e "  ${GREEN}⏭️  数据库 $DB_NAME 已存在，跳过导入${NC}"
fi

# ============================================================
# 4. 清理端口占用
# ============================================================
echo ""
echo -e "${CYAN}${BOLD}🔌 清理端口占用...${NC}"
if lsof -ti :$BACKEND_PORT > /dev/null 2>&1; then
    lsof -ti :$BACKEND_PORT | xargs kill -9 2>/dev/null
    echo -e "  ${YELLOW}⚠️  已清理端口 $BACKEND_PORT 占用${NC}"
else
    echo -e "  ${GREEN}✅ 端口 $BACKEND_PORT 空闲${NC}"
fi

if lsof -ti :$FRONTEND_PORT > /dev/null 2>&1; then
    lsof -ti :$FRONTEND_PORT | xargs kill -9 2>/dev/null
    echo -e "  ${YELLOW}⚠️  已清理端口 $FRONTEND_PORT 占用${NC}"
else
    echo -e "  ${GREEN}✅ 端口 $FRONTEND_PORT 空闲${NC}"
fi

if lsof -ti :$ADMIN_PORT > /dev/null 2>&1; then
    lsof -ti :$ADMIN_PORT | xargs kill -9 2>/dev/null
    echo -e "  ${YELLOW}⚠️  已清理端口 $ADMIN_PORT 占用${NC}"
else
    echo -e "  ${GREEN}✅ 端口 $ADMIN_PORT 空闲${NC}"
fi

# ============================================================
# 5. 安装前端依赖（如果 node_modules 不存在）
# ============================================================
echo ""
echo -e "${CYAN}${BOLD}📦 检查前端依赖...${NC}"
if [ ! -d "$PROJECT_DIR/frontend/node_modules" ]; then
    echo -e "  ${YELLOW}📥 正在安装前端依赖...${NC}"
    cd "$PROJECT_DIR/frontend" && pnpm install
    echo -e "  ${GREEN}✅ 前端依赖安装完成${NC}"
else
    echo -e "  ${GREEN}✅ 前端依赖已就绪${NC}"
fi

# ============================================================
# 5.1 安装管理端依赖（如果 node_modules 不存在）
# ============================================================
echo ""
echo -e "${CYAN}${BOLD}📦 检查管理端依赖...${NC}"
if [ ! -d "$PROJECT_DIR/arco-design-pro-vue/arco-design-pro-vite/node_modules" ]; then
    echo -e "  ${YELLOW}📥 正在安装管理端依赖...${NC}"
    cd "$PROJECT_DIR/arco-design-pro-vue/arco-design-pro-vite" && pnpm install
    echo -e "  ${GREEN}✅ 管理端依赖安装完成${NC}"
else
    echo -e "  ${GREEN}✅ 管理端依赖已就绪${NC}"
fi

# ============================================================
# 6. 后台启动后端 & 前端
# ============================================================
echo ""
echo -e "${CYAN}${BOLD}🚀 启动服务...${NC}"

# 启动后端
echo -e "  ${YELLOW}▶ 启动后端 (Spring Boot)...${NC}"
cd "$PROJECT_DIR/swim-admin"
mvn spring-boot:run -q > "$LOG_DIR/backend.log" 2>&1 &
echo $! > "$LOG_DIR/backend.pid"
echo -e "  ${GREEN}✅ 后端已在后台启动 (PID: $(cat $LOG_DIR/backend.pid))${NC}"

# 启动前端
echo -e "  ${YELLOW}▶ 启动前端 (Vite)...${NC}"
cd "$PROJECT_DIR/frontend"
pnpm dev > "$LOG_DIR/frontend.log" 2>&1 &
echo $! > "$LOG_DIR/frontend.pid"
echo -e "  ${GREEN}✅ 前端已在后台启动 (PID: $(cat $LOG_DIR/frontend.pid))${NC}"

# 启动管理端
echo -e "  ${YELLOW}▶ 启动管理端 (Arco Design Pro)...${NC}"
cd "$PROJECT_DIR/arco-design-pro-vue/arco-design-pro-vite"
pnpm dev -- --port $ADMIN_PORT > "$LOG_DIR/admin.log" 2>&1 &
echo $! > "$LOG_DIR/admin.pid"
echo -e "  ${GREEN}✅ 管理端已在后台启动 (PID: $(cat $LOG_DIR/admin.pid))${NC}"


# ============================================================
# 7. 打印角色信息
# ============================================================
echo ""
echo -e "${CYAN}${BOLD}════════════════════════════════════════════════════${NC}"
echo -e "${CYAN}${BOLD}  🏊 翔泳社游泳馆管理系统 · 启动成功${NC}"
echo -e "${CYAN}${BOLD}════════════════════════════════════════════════════${NC}"
echo ""
echo -e "  ${BOLD}📡 服务地址：${NC}"
echo -e "    后端 API:  ${GREEN}http://localhost:$BACKEND_PORT${NC}"
echo -e "    前端页面:  ${GREEN}http://localhost:$FRONTEND_PORT${NC}"
echo -e "    管理后台:  ${GREEN}http://localhost:$ADMIN_PORT${NC}"
echo ""
echo -e "  ${BOLD}👤 测试账号：${NC}"
echo -e "  ┌────────────┬────────────┬────────────┬──────────────┐"
echo -e "  │  ${BOLD}角色${NC}      │  ${BOLD}用户名${NC}    │  ${BOLD}密码${NC}      │  ${BOLD}姓名${NC}        │"
echo -e "  ├────────────┼────────────┼────────────┼──────────────┤"
echo -e "  │  管理员    │  admin     │  admin123  │  系统管理员  │"
echo -e "  │  管理员    │  admin02   │  admin123  │  王晓琳      │"
echo -e "  │  教练      │  coach01   │  coach123  │  李明辉      │"
echo -e "  │  教练      │  coach02   │  coach123  │  王海燕      │"
echo -e "  │  教练      │  coach03   │  coach123  │  赵文博      │"
echo -e "  │  教练      │  coach04   │  coach123  │  陈思怡      │"
echo -e "  │  教练      │  coach05   │  coach123  │  孙志强      │"
echo -e "  │  用户      │  user01    │  user123   │  张伟        │"
echo -e "  │  用户      │  user02    │  user123   │  刘婷        │"
echo -e "  │  用户      │  user03    │  user123   │  周浩然      │"
echo -e "  │  用户      │  user04    │  user123   │  吴佳琪      │"
echo -e "  │  用户      │  user05    │  user123   │  郑凯文      │"
echo -e "  │  用户      │  user06    │  user123   │  黄思涵      │"
echo -e "  │  用户      │  user07    │  user123   │  林子轩      │"
echo -e "  │  用户      │  user08    │  user123   │  杨雨萱      │"
echo -e "  └────────────┴────────────┴────────────┴──────────────┘"
echo ""
echo -e "  ${YELLOW}💡 提示: 按 Ctrl+C 可停止所有服务${NC}"
echo -e "${CYAN}${BOLD}════════════════════════════════════════════════════${NC}"
echo ""

# ============================================================
# 8. 实时合并日志输出
# ============================================================
echo -e "${CYAN}${BOLD}📋 实时日志输出（后端 + 前端）...${NC}"
echo ""

# 等待日志文件生成
sleep 2

# 使用 tail -f 同时追踪两个日志文件
tail -f "$LOG_DIR/backend.log" "$LOG_DIR/frontend.log" "$LOG_DIR/admin.log"
