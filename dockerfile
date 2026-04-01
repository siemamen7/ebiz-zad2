FROM kprzystalski/ebiznes-scala:latest

USER root

WORKDIR /app

COPY . .

RUN sbt compile

EXPOSE 9000

CMD ["sbt", "run"]