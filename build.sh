#!/bin/bash
docker run --rm -it -v ./:/work -v ./cache/buildozer:/home/builder/.buildozer -v ./cache/gradle:/home/builder/.gradle -w /work builder ./scripts/build.sh $@