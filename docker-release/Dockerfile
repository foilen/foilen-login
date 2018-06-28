FROM openjdk:8-jre-slim

RUN export TERM=dumb ; apt-get update && apt-get install -y \
    haproxy msmtp supervisor \
  && apt-get clean && rm -rf /var/lib/apt/lists/*

COPY app/ /app

EXPOSE 14010

WORKDIR /app
CMD java -jar foilen-login.jar
