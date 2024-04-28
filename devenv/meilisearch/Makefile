.PHONY:up
up:
	docker compose up -d

.PHONY:down
down:
	docker compose down

.PHONY:clean
clean:
	docker compose down -v

.PHONY:log
log:
	docker compose logs -f --since 1m
