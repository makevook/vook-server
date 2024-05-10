VERSION := $(shell git describe --tags --always --dirty)

.PHONY:deploy-dev
deploy-dev:
	dagger call -v deploy \
	--source-dir=../api \
	--profile=dev \
	--ssh-dest=file:./secrets/dev/dest.txt \
	--ssh-key=file:./secrets/dev/ssh.key \
	--target-path=~/repo/makevook/vook-deployment/dev \
	--version=${VERSION} \
	--command="make deploy-api"

.PHONY:build
build:
	dagger call -v build-api-jar --dir=../api --test -o out/api.jar
	dagger call -v build-api-image --jar-file=out/api.jar -o out/api_linux_arm64.tar
