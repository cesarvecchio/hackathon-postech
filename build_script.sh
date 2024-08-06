#!/bin/bash

make build-project
make build-docker

docker compose build
docker compose up -d