FROM ubuntu:24.04

RUN apt update && apt install -y capnproto pkg-config m4 libtool automake autoconf openjdk-17-jdk patchelf python3-venv unzip libssl-dev libgdbm-compat-dev liblzma-dev libbz2-dev cmake zlib1g-dev git python3-pip swig zip libffi-dev wget capnproto libcapnp-dev sudo gcc-arm-none-eabi libeigen3-dev

ARG USER_ID=1337
ARG GROUP_ID=1337

RUN groupadd -g $GROUP_ID builder && useradd -m -u $USER_ID -g $GROUP_ID builder && adduser builder sudo && echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

COPY capnproto-java /capnproto-java
WORKDIR /capnproto-java
RUN make && sudo make install

USER $USER_ID

RUN python3 -m venv ~/.venv
RUN ~/.venv/bin/pip install buildozer setuptools cython
RUN ~/.venv/bin/pip install scons numpy pycryptodome pycapnp
RUN ~/.venv/bin/pip install pyyaml certifi hatanaka casadi future-fstrings
