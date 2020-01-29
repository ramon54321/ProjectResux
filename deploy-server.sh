#!/usr/bin/env bash

source .env

echo Deploying Server

scp start-server.sh "$PRODUCTION_HOST":/home/ramon54321/projects/ProjectResux/
scp -r ./build/server "$PRODUCTION_HOST":/home/ramon54321/projects/ProjectResux/build
