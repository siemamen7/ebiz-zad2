#!/bin/bash

echo "Starting Docker containers..."
docker compose up -d

echo "Waiting for app to be ready on http://localhost:9000 ..."

# Wait until server responds with HTTP 200
until curl -s -o /dev/null -w "%{http_code}" http://localhost:9000/products | grep -q "200"; do
  echo "Waiting for the app to be ready..."
  sleep 2
done

echo "Starting ngrok..."
ngrok http 9000