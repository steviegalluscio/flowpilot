#!/bin/bash
mkdir -m 777 -p cache/buildozer cache/gradle
docker build --build-arg USER_ID=$(id -u) --build-arg GROUP_ID=$(id -g) -t builder docker/
