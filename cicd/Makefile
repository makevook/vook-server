VERSION := $(shell git describe --tags --always --dirty)

.PHONY:deploy-dev
deploy-dev:
	dagger call -v deploy \
	--source-dir=../server \
	--profile=dev \
	--ssh-dest=file:./secrets/dev/dest.txt \
	--ssh-key=file:./secrets/dev/ssh.key \
	--target-path=~/repo/makevook/vook-deployment/dev \
	--version=${VERSION} \
	--command="make deploy-api"

.PHONY:deploy-stag
deploy-stag:
	dagger call -v deploy \
	--source-dir=../server \
	--profile=stag \
	--ssh-dest=file:./secrets/stag/dest.txt \
	--ssh-key=file:./secrets/stag/ssh.key \
	--target-path=~/repo/makevook/vook-deployment/stag \
	--version=${VERSION} \
	--command="make deploy-api"

.PHONY:deploy-prod
deploy-prod:
	dagger call -v deploy \
	--source-dir=../server \
	--profile=prod \
	--ssh-dest=file:./secrets/prod/dest.txt \
	--ssh-key=file:./secrets/prod/ssh.key \
	--target-path=~/repo/makevook/vook-deployment/prod \
	--version=${VERSION} \
	--command="make deploy-api"

.PHONY:build-api-jar
build-api-jar:
	dagger call -v build-jar --dir=../server --test --sub-module=api export --path out/api.jar

.PHONY:build-api-image
build-api-image:
	dagger call -v build-image --jar-file=out/api.jar --name=api export --path out/api_linux_arm64.tar

.PHONY:run-api-jar
run-api-jar:
	(cd out && java -jar api.jar)

.PHONY:build-sync-jar
build-sync-jar:
	dagger call -v build-jar --dir=../server --test --sub-module=sync export --path out/sync.jar

.PHONY:run-sync-jar
run-sync-jar:
	(cd out && java -jar sync.jar)
