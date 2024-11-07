#!/bin/bash
docker build -t builder docker/
mkdir cache/buildozer cache/gradle
./build.sh flowy
