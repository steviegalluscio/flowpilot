#!/bin/bash

source ~/.venv/bin/activate

# python-for-android, first run to generate a python envirnoment scons can link against
if [ ! -d "flowy/.buildozer" ]; then
    cd flowy
    buildozer android debug
    cd -
fi

# For the host (x64)
## capnproto-java
cd third_party/capnproto-java
make
sudo make install
cd -

# For Android (arm64)
## libusb
~/.buildozer/android/platform/android-ndk-r25b/ndk-build -C third_party/libusb/android/jni USE_PC_NAME=1
## libjson11
~/.buildozer/android/platform/android-ndk-r25b/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android30-clang++ -shared third_party/json11/json11.cpp -fPIC -O3 -o android/libs/arm64-v8a/libjson11.so
## liblmdb
cd third_party/lmdb
make
cd -

# Build openpilot
scons .

# Rerun python-for-android, this time with all compiled resources
cd flowy
buildozer android debug
cd -

# Move Openpilot aar
cp flowy/bin/openpilot-0.1-arm64-v8a-debug.aar android/libs

# Move libs
cp third_party/libusb/android/libs/arm64-v8a/libusb-1.0.so android/libs/arm64-v8a
cp wrappers/libpandaflash.so android/libs/arm64-v8a
cp wrappers/libthneedrunner.so android/libs/arm64-v8a
cp cereal/libmessaging.so android/libs/arm64-v8a
cp selfdrive/modeld/libmodelparser.so android/libs/arm64-v8a
cp selfdrive/modeld/libthneed.so android/libs/arm64-v8a
cp selfdrive/boardd/libpandad.so android/libs/arm64-v8a
cp third_party/lmdb/liblmdb.so android/libs/arm64-v8a

# TODO: Move panda assets

# Gradle
ANDROID_HOME=/home/builder/.buildozer/android/platform/android-sdk ./gradlew assembleRelease
