#!/bin/bash

# python-for-android
source ~/.venv/bin/activate
cd flowy
buildozer android debug
cd -

# For the host (x64)
# capnproto-java
cd third_party/capnproto-java
make
sudo make install
cd -

# For Android (arm64)
# libusb
~/.buildozer/android/platform/android-ndk-r25b/ndk-build -C third_party/libusb/android/jni USE_PC_NAME=1

# Build openpilot
scons .

# Rerun flowy
cd flowy
buildozer android debug
cd -

# Move Openpilot
cp flowy/bin/openpilot-0.1-arm64-v8a-debug.aar android/libs

# Move libs
cp third_party/libusb/android/libs/arm64-v8a/libusb-1.0.so android/libs/arm64-v8a
cp wrappers/libpandaflash.so android/libs/arm64-v8a
cp wrappers/libthneedrunner.so android/libs/arm64-v8a

# TODO: Move panda assets

# Gradle
ANDROID_HOME=/home/builder/.buildozer/android/platform/android-sdk ./gradlew assembleRelease
