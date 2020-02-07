FROM alpine:3.11

ENV BUILD_DEPS \
    alpine-sdk \
    curl \
    binutils \
    cabal \
    coreutils \
    ghc \
    libffi-dev \
    jpeg-dev \
    musl-dev \
    python3-dev \
    zlib-dev

ENV PERSISTENT_DEPS \
    cairo-dev \
    gdk-pixbuf \
    gmp \
    graphviz \
    libffi \
    openjdk11-jre \
    pango-dev \
    python3 \
    py3-pip \
    sed \
    ttf-droid \
    ttf-droid-nonlatin

ENV PYTHONUNBUFFERED=1

ENV PLANTUML_VERSION 1.2019.8
ENV PLANTUML_DOWNLOAD_URL https://sourceforge.net/projects/plantuml/files/plantuml.$PLANTUML_VERSION.jar/download

ENV PANDOC_VERSION 2.7.3
ENV PANDOC_DOWNLOAD_URL https://hackage.haskell.org/package/pandoc-$PANDOC_VERSION/pandoc-$PANDOC_VERSION.tar.gz
ENV PANDOC_ROOT /usr/local/pandoc

ENV PATH $PANDOC_ROOT/bin:$PATH

# Install Dependencies
RUN apk --update add --no-cache $PERSISTENT_DEPS \
    && apk --update add --no-cache --virtual .build-deps $BUILD_DEPS \

# Install/Build Pandoc
    && curl -fsSL "$PLANTUML_DOWNLOAD_URL" -o /usr/local/plantuml.jar \
    && chmod a+r /usr/local/plantuml.jar \
    && mkdir -p /pandoc-build \
        /var/docs \
    && cd /pandoc-build \
    && curl -fsSL "$PANDOC_DOWNLOAD_URL" | tar -xzf - \
    && cd pandoc-$PANDOC_VERSION \
    && cabal update \
    && cabal install --only-dependencies \
    && cabal configure --prefix=$PANDOC_ROOT \
    && cabal build \
    && cabal copy \
    && cd / \
    && rm -Rf /pandoc-build \
        $PANDOC_ROOT/lib \
        /root/.cabal \
        /root/.ghc \
    && set -x \
    && addgroup -g 82 -S pandoc \
    && adduser -u 82 -D -S -G pandoc pandoc \
    && if [ ! -e /usr/bin/python ]; then ln -sf python3 /usr/bin/python ; fi && \
        python3 -m ensurepip && \
        rm -r /usr/lib/python*/ensurepip && \
        pip3 install --no-cache --upgrade pip setuptools wheel && \
        if [ ! -e /usr/bin/pip ]; then ln -s pip3 /usr/bin/pip ; fi \
    && pip3 install --no-cache weasyprint \
    && apk del --purge .build-deps

EXPOSE 8080:8080

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV SPRING_PROFILES_ACTIVE default

ENTRYPOINT ["java","-jar","/app.jar"]