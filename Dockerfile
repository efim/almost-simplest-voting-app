FROM hseeberger/scala-sbt
# For a Alpine Linux version, comment above and uncomment below:
# FROM 1science/sbt

RUN mkdir -p /voting-app

WORKDIR /voting-app

COPY ./almost-simplest-election-app /voting-app
