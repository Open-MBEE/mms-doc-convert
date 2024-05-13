FROM pandoc/core:2.19

ENV BUILD_DEPS \
    msttcorefonts-installer

ENV PERSISTENT_DEPS \
    openjdk11-jre \
    python3 \
    curl
ENV PYTHONUNBUFFERED=1

ENV PLANTUML_VERSION 1.2019.8
ENV PLANTUML_DOWNLOAD_URL https://sourceforge.net/projects/plantuml/files/plantuml.$PLANTUML_VERSION.jar/download
ENV PRINCE_DOWNLOAD_URL https://www.princexml.com/download/prince-15.1-alpine3.14-x86_64.tar.gz
ENV DEJAVU_DOWNLOAD http://sourceforge.net/projects/dejavu/files/dejavu/2.37/dejavu-fonts-ttf-2.37.tar.bz2
ENV PATH $PANDOC_ROOT/bin:$PATH

# Install Dependencies
RUN apk --update add --no-cache $PERSISTENT_DEPS \
    && apk --update add --no-cache --virtual .build-deps $BUILD_DEPS \
    && update-ms-fonts \
    && curl -fsSL "$DEJAVU_DOWNLOAD" -o /tmp/font.tar.bz2 \
    && tar -xjvf /tmp/font.tar.bz2 -C /tmp \
    && cp -r /tmp/dejavu-fonts-ttf-2.37/ttf /usr/share/fonts/dejavu \
    && cp /tmp/dejavu-fonts-ttf-2.37/fontconfig/* /usr/share/fontconfig/conf.avail/ \
    && fc-cache -f \
    # Install/Build Pandoc
    && curl -fsSL "$PLANTUML_DOWNLOAD_URL" -o /usr/local/plantuml.jar \
    && chmod a+r /usr/local/plantuml.jar \
    && if [ ! -e /usr/bin/python ]; then ln -sf python3 /usr/bin/python ; fi && \
        python3 -m ensurepip && \
        rm -r /usr/lib/python*/ensurepip && \
        pip3 install --no-cache --upgrade pip setuptools wheel && \
        if [ ! -e /usr/bin/pip ]; then ln -s pip3 /usr/bin/pip ; fi \
    && pip3 install --no-cache weasyprint \
    && curl -fsSL "$PRINCE_DOWNLOAD_URL" -o /tmp/prince.tar.gz \
    && tar -zxf /tmp/prince.tar.gz -C /tmp \
    && sh /tmp/prince-15.1-alpine3.14-x86_64/install.sh \
    && rm -rf /tmp/prince* \
    && rm -rf /tmp/dejavu* \
    && apk del --purge .build-deps

EXPOSE 8080:8080

COPY *.jar /app.jar

ENV SPRING_PROFILES_ACTIVE default

ENTRYPOINT ["java","-jar","/app.jar"]
