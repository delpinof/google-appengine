# Getting Started

## Prerequisites

- JDK 17
- Docker
- MongoDB 4+

### MongoDB

https://hub.docker.com/r/mongodb/mongodb-community-server/tags

```bash
docker run --name mongodb -p 27017:27017 -v mongodb:/data/db mongodb/mongodb-community-server:latest
```

## Build

```bash
./mvnw clean install
```

## Run

- You can run directly from Intellij once mongodb is up.
- Or use docker compose:
- For local development, set these environment variables before starting the app:

- `MONGODB_URI`
- `MONGODB_NAME`
- `JWT_SECRET`

Example:

```bash
export MONGODB_URI='mongodb://localhost:27017'
export MONGODB_NAME='google-appengine'
export JWT_SECRET='replace-with-a-local-secret'
./mvnw spring-boot:run
```

```bash
docker volume create mongodb
docker compose up
```

## Optional build & run

build docker image:
```bash
docker build -t fherdelpino/google-appengine .
```
run docker container:
```bash
docker run -p 8080:8080 -e SPRING_DATA_MONGODB_URI='mongodb://{mongodb_ip}:27017' --name google-appengine fherdelpino/google-appengine
```
## Documentation

Swagger documentation at:

http://localhost:8080/swagger-ui/index.html


## Deploy

### GCP

#### App engine

Before deploying, update [`app.yaml`](/Users/fherdelpino/workspaces/personal/google-appengine/app.yaml) with the real
values for:

- `MONGODB_URI`
- `MONGODB_NAME`
- `JWT_SECRET`

`gcloud app deploy` automatically picks up `app.yaml` from the project root.

```bash
gcloud app deploy --quiet --no-cache
```
