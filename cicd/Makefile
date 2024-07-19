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

.PHONY:build-jar
build-jar:
	dagger call -v build-api-jar --dir=../server --test --sub-module=api export --path out/api.jar

.PHONY:build-image
build-image: build-jar
	dagger call -v build-api-image --jar-file=out/api.jar export --path out/api_linux_arm64.tar

.PHONY:run-jar
run-jar:
	(cd out && java -jar api.jar)
