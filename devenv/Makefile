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

.PHONY:create-dump-meilisearch
create-dump-meilisearch:
	curl -X POST 'http://localhost:7700/dumps' \
		-H 'Authorization: Bearer aSampleMasterKey'

.PHONY:copy-dump-meilisearch
copy-dump-meilisearch:
	mkdir -p ./temp/meilisearch
	docker compose cp meilisearch:/meili_data/dumps ./temp/meilisearch

MEILISEARCH_DUMP_FILE=
.PHONY:restore-dump-meilisearch
restore-dump-meilisearch:
	docker compose down meilisearch -v
	MEILISEARCH_DUMP_FILE=${MEILISEARCH_DUMP_FILE} \
		docker compose -f compose.yml -f compose.meilisearch-restore.yml up meilisearch
