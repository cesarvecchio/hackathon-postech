#!/bin/bash

make build-project
make build-docker

make compose-build
make compose-up