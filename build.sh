#!/bin/bash
if [ -t 1 ]
then
    docker run --rm -it -v $PWD:/work -v $PWD/cache/buildozer:/home/builder/.buildozer -v $PWD/cache/gradle:/home/builder/.gradle -w /work builder bash scripts/build.sh $@
else
    docker run --rm -v $PWD:/work -v $PWD/cache/buildozer:/home/builder/.buildozer -v $PWD/cache/gradle:/home/builder/.gradle -w /work builder bash scripts/build.sh $@
fi
