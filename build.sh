#!/bin/bash
docker run --rm -it -u `id -u`:`id -g` -v ./:/work -v ./cache/buildozer:/home/builder/.buildozer -v ./cache/gradle:/home/builder/.gradle -w /work builder bash scripts/build.sh $@
