build-project:
	gradle build --refresh-dependencies -x test

build-docker:
	docker build .