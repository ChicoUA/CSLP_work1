# Dockerfile

FROM ubuntu:latest

COPY ./Work/Aplicacao.class /usr/Work/
COPY ./Work/BloomFilter.class /usr/Work/
COPY ./Work/Contador.class /usr/Work/
COPY ./Work/InitHashFunction.class /usr/Work/
COPY ./Work/Modulo3.class /usr/Work/
COPY ./Work/Signature.class /usr/Work/
COPY ./Work/testeContador.class /usr/Work/
COPY ./Work/Testes.class /usr/Work/
COPY ./text1 /usr/
COPY ./text2.txt /usr/
COPY ./text3 /usr/

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME


WORKDIR /usr

EXPOSE 8080

CMD java -ea Work.Modulo3





