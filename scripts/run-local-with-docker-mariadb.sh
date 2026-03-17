#!/usr/bin/env bash
#
# Запуск srvlog UI локально (MariaDB на хосте).
#
# Использование: ./scripts/run-local-with-docker-mariadb.sh
#
# Переменные окружения:
#   MARIADB_ROOT_PWD  — пароль root в MariaDB (пустой = без пароля)
#   MARIADB_PORT      — порт MariaDB (по умолчанию 3306)
#   SKIP_INSTALL      — пропустить install/install.sh (если уже выполнено)
#   RUN_ONLY_UI       — запустить только UI (пропустить install, init_db, migrations)
#

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
MARIADB_ROOT_PWD="${MARIADB_ROOT_PWD:-}"
MARIADB_PORT="${MARIADB_PORT:-3306}"

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info()  { echo -e "${GREEN}[INFO]${NC} $*"; }
log_warn()  { echo -e "${YELLOW}[WARN]${NC} $*"; }
log_error() { echo -e "${RED}[ERROR]${NC} $*"; }

# Установка зависимостей (install/install.sh)
run_install() {
    if [[ -n "${SKIP_INSTALL:-}" ]]; then
        log_info "Пропуск install (SKIP_INSTALL=1)"
        return
    fi
    log_info "Установка зависимостей (install/install.sh)..."
    cd "$PROJECT_ROOT"
    local inst="$PROJECT_ROOT/install"
    ./mvnw install:install-file -DgroupId=org.sphx.api -DartifactId=sphinx-api -Dpackaging=jar -Dversion=3.6.1 -Dfile="$inst/sphinxapi.jar" -DgeneratePom=true -q
    ./mvnw install:install-file -DgroupId=com.nesscomputing -DartifactId=ness-syslog4j -Dpackaging=jar -Dversion=0.9.47-NESS-8-SNAPSHOT -Dfile="$inst/ness-syslog4j-0.9.47-NESS-8-SNAPSHOT.jar" -DpomFile="$inst/ness-syslog4j-0.9.47-NESS-8-SNAPSHOT.pom" -q
    ./mvnw install:install-file -DgroupId=mysql -DartifactId=mysql-connector-java -Dpackaging=jar -Dversion=5.1.22-3 -Dfile="$inst/mysql-connector-java-5.1.22-3-bin.jar" -DgeneratePom=true -q
    log_info "Зависимости установлены."
}

# Инициализация БД
init_db() {
    log_info "Инициализация БД srvlog (порт $MARIADB_PORT)..."
    local mysql_opts=(-h 127.0.0.1 -P "$MARIADB_PORT" -u root)
    [[ -n "$MARIADB_ROOT_PWD" ]] && mysql_opts+=(-p"$MARIADB_ROOT_PWD")

    mysql "${mysql_opts[@]}" <<'EOSQL'
drop database if exists srvlog;
create database `srvlog` default character set utf8 collate utf8_general_ci;

grant all privileges on srvlog.* to 'srvlog'@'localhost' identified by '123srvlog123' with grant option;
grant all privileges on srvlog.* to 'srvlog'@'127.0.0.1' identified by '123srvlog123' with grant option;
grant all privileges on srvlog.* to 'srvlog'@'%' identified by '123srvlog123' with grant option;

grant grant option on srvlog.* to 'srvlog'@'localhost';
grant grant option on srvlog.* to 'srvlog'@'127.0.0.1';
grant grant option on srvlog.* to 'srvlog'@'%';

flush privileges;
EOSQL
    log_info "БД srvlog создана."
}

# Проверка доступности MariaDB
check_mysql() {
    log_info "Проверка MariaDB на localhost:$MARIADB_PORT..."
    local max_attempts=15
    local attempt=1
    while [[ $attempt -le $max_attempts ]]; do
        if mysql -h 127.0.0.1 -P "$MARIADB_PORT" -u srvlog -p123srvlog123 -e "select 1" &>/dev/null; then
            log_info "MariaDB доступна."
            return 0
        fi
        sleep 1
        ((attempt++)) || true
    done
    log_error "Не удалось подключиться к MariaDB"
    exit 1
}

# Flyway миграции
run_migrations() {
    log_info "Запуск Flyway миграций..."
    cd "$PROJECT_ROOT"
    ./mvnw flyway:migrate -pl srvlog-sql -Dflyway.url="jdbc:mariadb://localhost:${MARIADB_PORT}/srvlog?characterEncoding=utf8&useInformationSchema=true&noAccessToProcedureBodies=true&useLocalSessionState=true&autoReconnect=false" -q
    log_info "Миграции выполнены."
}

# Запуск UI (с подстановкой порта в jetty-env)
run_ui() {
    log_info "Запуск srvlog UI..."
    log_info "Откройте: http://localhost:8080/srvlog/main"
    log_info "Для остановки нажмите Ctrl+C"

    local jetty_env="${TMPDIR:-/tmp}/jetty-env-ui-$$.xml"
    sed "s|jdbc:mysql://localhost/srvlog|jdbc:mysql://localhost:${MARIADB_PORT}/srvlog|g" \
        "$PROJECT_ROOT/srvlog-web/src/test/resources/jetty/jetty-env-ui.xml" > "$jetty_env"
    trap "rm -f $jetty_env" EXIT

    cd "$PROJECT_ROOT"
    exec env JETTY_ENV_XML="$jetty_env" ./mvnw exec:java -Dexec.mainClass="com.payneteasy.StartUI" -Dexec.classpathScope=test -Dsrvlog.web.dir="$PROJECT_ROOT/srvlog-web" -pl srvlog-web -q
}

# --- main ---
main() {
    cd "$PROJECT_ROOT"
    log_info "Проект: $PROJECT_ROOT"

    if [[ -n "${RUN_ONLY_UI:-}" ]]; then
        log_info "Режим RUN_ONLY_UI — только запуск UI"
        run_ui
        return
    fi

    run_install
    init_db
    check_mysql
    run_migrations
    run_ui
}

main "$@"
