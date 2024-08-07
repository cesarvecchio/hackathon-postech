build-project:
	gradle build --refresh-dependencies -x test

build-docker:
	docker build .

compose-build:
	docker compose build

compose-up:
	docker compose up -d

unit-test: gradle-clean
	gradle unitTest

gradle-clean:
	gradle clean