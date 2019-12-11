# See https://docs.docker.com/engine/userguide/eng-image/multistage-build/

FROM debian:stretch

ARG COUNTRY=de
ARG LOC_VAL=${COUNTRY}_DE
ARG ENCODING=UTF-8
RUN apt-get update && DEBIAN_FRONTEND=noninteractive \
      apt-get install \
        openjdk-8-jre-headless \
        ca-certificates-java \
        locales \
      --assume-yes

RUN sed -i -e 's/# ${LOC_VAL}.UTF-8 ${ENCODING}/${LOC_VAL}.${ENCODING} ${ENCODING}/' /etc/locale.gen && \
    dpkg-reconfigure --frontend=noninteractive locales && \
    update-locale LANG=${LOC_VAL}.${ENCODING}

ENV LANG ${LOC_VAL}.${ENCODING}
ENV LANGUAGE ${LOC_VAL}:${COUNTRY}
ENV LC_ALL ${LOC_VAL}.${ENCODING}
