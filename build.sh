#!/bin/bash
if [ -t 1 ]
then
    docker run --rm -it -u `id -u`:`id -g` -v $PWD:/work -v $PWD/cache/buildozer:/home/builder/.buildozer -v $PWD/cache/gradle:/home/builder/.gradle -w /work builder bash scripts/build.sh $@
else
    docker run --rm -u `id -u`:`id -g` -v $PWD:/work -v $PWD/cache/buildozer:/home/builder/.buildozer -v $PWD/cache/gradle:/home/builder/.gradle -w /work builder bash scripts/build.sh $@
fi
