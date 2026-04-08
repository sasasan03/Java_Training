.PHONY: up down db-shell build run logs ps clean help

# Docker Compose
up:
	docker compose up -d

down:
	docker compose down

# DBコンテナに入る
db-shell:
	docker exec -it java_training_db psql -U student -d training

# Javaビルド＆起動
build:
	docker compose build

run:
	docker compose up --build

# ログ確認
logs:
	docker compose logs -f

logs-db:
	docker compose logs -f db

# コンテナ状態確認
ps:
	docker compose ps

# データボリュームごと削除（DBリセット）
clean:
	docker compose down -v

# ヘルプ
help:
	@echo "使用可能なコマンド:"
	@echo "  make up        - コンテナをバックグラウンドで起動"
	@echo "  make down      - コンテナを停止"
	@echo "  make db-shell  - DBコンテナ(psql)に入る"
	@echo "  make build     - Dockerイメージをビルド"
	@echo "  make run       - ビルドして起動"
	@echo "  make logs      - 全コンテナのログを表示"
	@echo "  make logs-db   - DBのログを表示"
	@echo "  make ps        - コンテナの状態を確認"
	@echo "  make clean     - コンテナとDBデータを削除"
