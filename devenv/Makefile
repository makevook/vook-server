.PHONY:up
up:
	docker compose up -d --build

.PHONY:down
down:
	docker compose down

.PHONY:log
log:
	docker compose logs -f

.PHONY:clean
clean:
	docker compose down -v
