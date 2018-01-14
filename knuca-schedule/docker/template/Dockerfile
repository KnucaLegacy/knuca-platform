FROM openjdk:8u151-jre

WORKDIR /knuca-platform

ENV TAR

COPY knuca-platform/${TAR} knuca-platform/app.tgz

RUN mkdir knuca-platform/app \
        && tar -xvzf knuca-platform/app.tgz --strip-components=1 -C knuca-platform/app

EXPOSE 8080

ENV NAME knuca-platform

ENTRYPOINT knuca-platform/app/bin/knuca-rest
