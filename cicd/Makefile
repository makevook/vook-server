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

.PHONY:deploy-stag
deploy-stag:
	dagger call -v deploy \
	--source-dir=../api \
	--profile=stag \
	--ssh-dest=file:./secrets/stag/dest.txt \
	--ssh-key=file:./secrets/stag/ssh.key \
	--target-path=~/repo/makevook/vook-deployment/stag \
	--version=${VERSION} \
	--command="make deploy-api"

.PHONY:build-jar
build-jar:
	dagger call -v build-api-jar --dir=../api --test -o out/api.jar

.PHONY:build-image
build-image: build-jar
	dagger call -v build-api-image --jar-file=out/api.jar -o out/api_linux_arm64.tar

.PHONY:run-jar
run-jar:
	(cd out && java -jar api.jar)
