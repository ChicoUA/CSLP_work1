FROM ubuntu:latest

COPY . /usr/C_app/

ENV DEBIAN_FRONTEND noninteractive
RUN apt-get update && \
    apt-get -y install gcc mono-mcs && \
    rm -rf /var/lib/apt/lists/*
RUN apt-get clean && apt-get update && apt-get install -y locales
RUN locale-gen pt_PT.UTF-8


WORKDIR /usr/C_app

EXPOSE 8080

CMD ./tsp
