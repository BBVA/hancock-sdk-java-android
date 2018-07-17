.PHONY: build-dev dev test publish docs shell down-dev coverage

YML_DEV=environment/dev/docker-compose.yml
COMPOSE_DEV=docker-compose -f ${YML_DEV}

build-dev:
	${COMPOSE_DEV} build

build: build-dev down-dev
	${COMPOSE_DEV} run --rm --service-ports hancock_sdk_client_java build

test: build-dev down-dev
	${COMPOSE_DEV} run --rm --service-ports hancock_sdk_client_java test

coverage: build-dev down-dev
	${COMPOSE_DEV} run --rm --service-ports hancock_sdk_client_java coverage

publish: build-dev down-dev
	${COMPOSE_DEV} run --rm --service-ports hancock_sdk_client_java publish

docs: build-dev down-dev
	${COMPOSE_DEV} run --rm --service-ports hancock_sdk_client_java docs

shell: build-dev down-dev
	${COMPOSE_DEV} run --rm --no-deps hancock_sdk_client_java /bin/bash

down-dev:
	${COMPOSE_DEV} down