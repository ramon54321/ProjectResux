#!/usr/bin/env bash

source .env

echo Deploying HTTP

scp start-http.sh "$PRODUCTION_HOST":/home/ramon54321/projects/ProjectResux/
scp -r ./build/http "$PRODUCTION_HOST":/home/ramon54321/projects/ProjectResux/build
