#!/bin/bash
if [ -t 1 ]
then
    docker run --rm -it -v $PWD:/work -v $PWD/cache/buildozer:/root/.buildozer -v $PWD/cache/gradle:/root/.gradle -w /work builder bash scripts/build.sh $@
else
    docker run --rm -v $PWD:/work -v $PWD/cache/buildozer:/root/.buildozer -v $PWD/cache/gradle:/root/.gradle -w /work builder bash scripts/build.sh $@
fi
