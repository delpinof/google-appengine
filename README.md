# Getting Started

## Prerequisites

- JDK 17
- MongoDB 4+

### MongoDB

https://hub.docker.com/r/mongodb/mongodb-community-server/tags

```bash
docker run --name mongodb -d -p 27017:27017 mongodb/mongodb-community-server:latest
```

## Build

```bash
./mvnw clean install
```

## Deploy

### GCP

```bash
gcloud app deploy --quiet
```