#!/usr/bin/env bash

source .env

echo Deploying Client

scp -r ./build/client "$PRODUCTION_HOST":/home/ramon54321/projects/ProjectResux/build
