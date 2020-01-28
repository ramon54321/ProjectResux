#!/usr/bin/env bash

source .env

echo Deploying Server

scp -r ./build/server "$PRODUCTION_HOST":/home/ramon54321/projects/ProjectResux/build
