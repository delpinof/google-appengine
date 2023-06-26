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

```bash
docker volume create mongodb
docker compose up
```

## Deploy

### GCP

#### App engine

```bash
gcloud app deploy --quiet
```

#### Cloud build

```bash
gcloud builds submit --region=us-central1
```