FROM alpine:3.10

ENV BUILD_DEPS \
    alpine-sdk \
    cabal \
    coreutils \
    ghc \
    libffi \
    musl-dev \
    zlib-dev

ENV PERSIST_DEPS \
    gmp \
    graphviz \
    openjdk11 \
    python \
    py2-pip \
    sed \
    ttf-droid \
    ttf-droid-nonlatin

ENV LANG en_US.utf8

ENV USR_LOCAL /usr/local

ENV PLANTUML_VERSION 1.2019.8
ENV PLANTUML_DOWNLOAD_URL https://sourceforge.net/projects/plantuml/files/plantuml.$PLANTUML_VERSION.jar/download

ENV PANDOC_VERSION 2.7.3
ENV PANDOC_DOWNLOAD_URL https://hackage.haskell.org/package/pandoc-$PANDOC_VERSION/pandoc-$PANDOC_VERSION.tar.gz
ENV PANDOC_ROOT $USR_LOCAL/pandoc

ENV JAVA_HOME /usr/lib/jvm/default-jvm
ENV PATH $PATH:$PANDOC_ROOT/bin:$JAVA_HOME/bin

# Install/Build Packages
RUN apk upgrade --update && \
#    apk add --virtual .build-deps $BUILD_DEPS && \
    apk add --virtual .persistent-deps $PERSISTENT_DEPS
#    curl -fsSL "$PLANTUML_DOWNLOAD_URL" -o /usr/local/plantuml.jar && \
#    chmod a+r /usr/local/plantuml.jar && \
#    mkdir -p /pandoc-build \
#             /var/docs && \
#    cd /pandoc-build && \
#    curl -fsSL "$PANDOC_DOWNLOAD_URL" | tar -xzf - && \
#    cd pandoc-$PANDOC_VERSION && \
#    cabal update && \
#    cabal install --only-dependencies && \
#    cabal configure --prefix=$PANDOC_ROOT && \
#    cabal build && \
#    cabal copy && \
#    cd / && \
#    rm -Rf /pandoc-build \
#           $PANDOC_ROOT/lib \
#           /root/.cabal \
#           /root/.ghc && \
#    set -x && \
#    addgroup -g 82 -S pandoc && \
#    adduser -u 82 -D -S -G pandoc pandoc && \
#    apk del .build-deps
RUN ls -al /usr/lib
RUN echo '#!/bin/sh'; echo 'set -e'; echo; echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"' > /usr/local/bin/docker-java-home && \
    chmod +x /usr/local/bin/docker-java-home
RUN sh /usr/local/bin/docker-java-home

EXPOSE 8080:8080

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]